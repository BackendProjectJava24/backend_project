package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService implements BookingServiceImp {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, PlaceRepository placeRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
        User user = getCurrentAuthenticatedUser();
        Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        long nights = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());
        double totalAmount = nights * place.getPrice();

        Booking newBooking = new Booking();
        newBooking.setCustomer(user);
        newBooking.setPlace(place);
        newBooking.setCheckInDate(bookingDTO.getCheckInDate());
        newBooking.setCheckOutDate(bookingDTO.getCheckOutDate());
        newBooking.setGuests(bookingDTO.getGuests());
        newBooking.setTotalAmount(totalAmount);
        newBooking.setCreatedAt(String.valueOf(LocalDate.now()));

        Booking savedBooking = bookingRepository.save(newBooking);
        return convertBookingToBookingDTO(savedBooking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return List.of();
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByCustomerId(String customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        return bookings.stream().map(this::convertBookingToBookingDTO).toList();
    }

    @Override
    public BookingResponseDTO getBookingById(String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));
        return convertBookingToBookingDTO(booking);
    }
    @Override
    public BookingResponseDTO updateBooking(String id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Booking not found"));

        if (bookingDTO.getCheckInDate() != null) booking.setCheckInDate(bookingDTO.getCheckInDate());
        if (bookingDTO.getCheckOutDate() != null) booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        if (bookingDTO.getGuests() > 0) booking.setGuests(bookingDTO.getGuests());

        Booking updatedBooking = bookingRepository.save(booking);
        return convertBookingToBookingDTO(updatedBooking); // ✅ Now returns BookingResponseDTO
    }

    private BookingResponseDTO convertBookingToBookingDTO(Booking booking) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setCustomerId(booking.getCustomer().getId());
        bookingResponseDTO.setBookedPlace(booking.getPlace());
        bookingResponseDTO.setCheckInDate(booking.getCheckInDate());
        bookingResponseDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingResponseDTO.setNights((int) ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate()));
        bookingResponseDTO.setGuests(booking.getGuests());
        bookingResponseDTO.setTotalAmount(booking.getTotalAmount());
        bookingResponseDTO.setCreatedAt(booking.getCreatedAt().toString());
        return bookingResponseDTO;
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}