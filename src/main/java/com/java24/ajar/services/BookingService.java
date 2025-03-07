package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponse;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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

    // Add the authenticate method
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    @Override
    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
        User user = getCurrentAuthenticatedUser();

        Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        // Validate dates against availability
        if (!isPlaceAvailable(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new IllegalArgumentException("Place is not available for selected dates");
        }

        // Validate guests count
        if (bookingDTO.getGuests() > place.getGestt()) {
            throw new IllegalArgumentException("Number of guests exceeds place capacity");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // Calculate total amount
        double totalAmount = nights * place.getPrice();

        Booking newBooking = new Booking();
        newBooking.setCustomer(user);
        newBooking.setPlace(place);
        newBooking.setCheckInDate(bookingDTO.getCheckInDate());
        newBooking.setCheckOutDate(bookingDTO.getCheckOutDate());
        newBooking.setGuests(bookingDTO.getGuests());
        newBooking.setTotalAmount(totalAmount);
        LocalDate creationDate = LocalDate.now();
        newBooking.setCreatedAt(creationDate);

        return convertBookingToBookingDTO(bookingRepository.save(newBooking));
    }

    // get only the definded information
    private BookingResponseDTO convertBookingToBookingDTO(Booking booking) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setCustomer(booking.getCustomer());
        bookingResponseDTO.setBookedPlace(booking.getPlace());
        bookingResponseDTO.setChickInDate(booking.getCheckInDate());
        bookingResponseDTO.setChickOutDate(booking.getCheckOutDate());

        // count the nights between the check in date and check out date
        Period period = Period.between(booking.getCheckInDate(), booking.getCheckOutDate());
        long nights =  period.getDays();
        bookingResponseDTO.setNights((int) nights);

        bookingResponseDTO.setGests(bookingResponseDTO.getGests());
        bookingResponseDTO.setTotalAmount(booking.getTotalAmount());
        bookingResponseDTO.setCreatedAt(booking.getCreatedAt());

        return bookingResponseDTO;
    }

    // the result is true all the time i need more help her to define the problem
    private boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        List<AvailabilityPeriod> existingPeriods = place.getAvailability();
        boolean isAvailable = false;
        for (AvailabilityPeriod existingPeriod : existingPeriods) {
            LocalDate startDateAvailable = existingPeriod.getStartDate();
            LocalDate endDateAvailable = existingPeriod.getEndDate();
            if ((startDate.isEqual(startDateAvailable) || startDate.isAfter(startDateAvailable) && startDate.isBefore(endDate))
                    &&(endDate.isEqual(endDateAvailable) || endDate.isBefore(endDateAvailable)) ) {
                //  overlaps found
               isAvailable = true;  // the place is not avaible
            }
        }
        //  overlaps found
        return isAvailable; // Place is available
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();
        for(Booking booking : bookings) {
            BookingResponseDTO bookingResponseDTO = convertBookingToBookingDTO(booking);
            bookingResponseDTOs.add(bookingResponseDTO);
        }
        return bookingResponseDTOs;
    }

    @Override
    public List<BookingResponseDTO> getAllBookingsByCustomerId(String customerId) {
List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
if (bookings.isEmpty() || bookings == null) {
    throw new NoSuchElementException("User is not authenticated");
}
List<BookingResponseDTO> bookingResponseDTOs = new ArrayList<>();
for(Booking booking : bookings) {
    BookingResponseDTO bookingResponseDTO = convertBookingToBookingDTO(booking);
    bookingResponseDTOs.add(bookingResponseDTO);
}
if (bookingResponseDTOs.isEmpty() || bookingResponseDTOs == null) {
    throw new NoSuchElementException("User is not authenticated");
}
        return bookingResponseDTOs;
    }

    @Override
    public BookingResponseDTO getBookingById(String id) {
        return null;
    }
}
