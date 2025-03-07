package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingItemDTO;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

//     skapa en ny order
//     byter typ när vi gjort OrderResponseDTO från Order

    //    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
//            throw new UnauthorizedException("User is not authenticated");
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//
//        List<Place> places = new ArrayList<>();
//        Map<String, Integer> quantities = new HashMap<>();
//        double totalAmount = 0.0;
//
//        for (BookingItemDTO itemDTO : bookingDTO.getItems()) {
//            Place place = placeRepository.findById(itemDTO.getPlaceId())
//                    .orElseThrow(() -> new IllegalArgumentException("Place not found"));
//
//
//            places.add(place);
//            quantities.put(itemDTO.getPlaceId(), itemDTO.getQuantity());
//            totalAmount += place.getPrice() * itemDTO.getQuantity();
//        }
//
//        Booking newBooking = new Booking();
//        newBooking.setCustomer(user);
//        newBooking.setItems(places);
//        newBooking.setQuantities(quantities);
//        newBooking.setTotalAmount(totalAmount);
//
//        Booking savedBooking = bookingRepository.save(newBooking);
//
//        return converToBookingResponseDTO(savedBooking);
//    }
    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());


    }


    @Override
    public List<Booking> getAllBookingsByCustomerId(String customerId) {
        User user = getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("User has not any bookings");
        }

        return bookings;
    }

    @Override
    public BookingResponseDTO getBookingById(String id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            throw new UnauthorizedException("User is not any booking");
        }
        return converToBookingResponseDTO1(booking);
    }

    public List<Booking> getBookingsByUser() {
        User user = getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByCustomerId(user.getId());
        if (bookings.isEmpty()) {
            throw new IllegalArgumentException("User has not any bookings");
        }

        return bookings;
    }


    @Override
    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {
        // chech if the user is authenticated
        User user = getCurrentAuthenticatedUser();
        List<Place> places = new ArrayList<>();
        Map<String, Integer> quantities = new HashMap<>();
        double totalAmount = 0.0;

        for (BookingItemDTO itemDTO : bookingDTO.getItems()) {
            Place place = placeRepository.findById(itemDTO.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Place not found"));

            // check if the place is avablw to booking by the method placeIsavableToBooking
            // the method is not worked
            if (placeIsAvailableToBooking(place,bookingDTO.getBookingPeriod())){
                places.add(place);
            }

        }
        if (places.isEmpty()) {
            throw new IllegalArgumentException("place is not available to the booking");
        }
        Booking newBooking = new Booking();
        newBooking.setCustomer(user);
        newBooking.setBookingPeriod(bookingDTO.getBookingPeriod());
        newBooking.setItems(places);
        newBooking.setTotalAmount(totalAmount);
        newBooking.setQuantities(quantities);

        Booking savedBooking = bookingRepository.save(newBooking);
        return converToBookingResponseDTO1(savedBooking);
    }
// the result is true all the time i need more help her to define the problem
    private boolean placeIsAvailableToBooking(Place place, AvailabilityPeriod availabilityPeriod) {
        LocalDate startDate = availabilityPeriod.getStartDate();
        LocalDate endDate = availabilityPeriod.getEndDate();
        List<AvailabilityPeriod> existingPeriods = place.getAvailability();

        for (AvailabilityPeriod existingPeriod : existingPeriods) {
            LocalDate existingStartDate = existingPeriod.getStartDate();
            LocalDate existingEndDate = existingPeriod.getEndDate();

            if (startDate.isBefore(existingEndDate) && endDate.isAfter(existingStartDate)) {
                // There is an overlap
                return false;
            }
        }
        // No overlaps found
        return true;
    }



    public BookingResponseDTO converToBookingResponseDTO1(Booking booking) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();
        bookingResponseDTO.setId(booking.getId());
        bookingResponseDTO.setCustomerId(booking.getCustomer().getId());
        List<Place> places = booking.getItems();
        List<BookingItemDTO> bookingItemDTOS = new ArrayList<>();
        for (Place place : places) {
            BookingItemDTO bookingItemDTO = new BookingItemDTO();
            bookingItemDTO.setName(place.getName());
            bookingItemDTO.setPrice(place.getPrice());
            bookingItemDTO.setQuantity(place.getGestt());
            bookingItemDTO.setPlaceId(place.getId());
            bookingItemDTOS.add(bookingItemDTO);
        }
        bookingResponseDTO.setItems(bookingItemDTOS);
        return bookingResponseDTO;
    }

    private BookingResponseDTO converToBookingResponseDTO(Booking booking) {
        List<BookingItemDTO> itemDTOs = booking.getItems().stream()
                .map(product -> {
                    String productId = product.getId();
                    String name = product.getName();
                    double price = product.getPrice();
                    int quantity = booking.getQuantities().getOrDefault(productId, 0);
                    return new BookingItemDTO(productId, name, price, quantity);
                })
                .collect(Collectors.toList());

        Map<String, String> quantities = booking.getQuantities().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey(),
                        entry -> placeRepository.findById(entry.getKey())
                                .map(Place::getName)
                                .orElse("Product not found")
                ));
        return new BookingResponseDTO(
                booking.getId(),
                booking.getCustomer().getId(),
                booking.getTotalAmount(),
                itemDTOs,
                quantities,
                booking.getCreatedAt().toString()
        );
    }

    private BookingResponse convertToDTO(Booking booking) {
        BookingResponse bookingResponse = new BookingResponse();

        bookingResponse.setUserId(booking.getCustomer().getId());

        bookingResponse.setBookingPlaceIds(
                booking.getItems().stream()
                        .map(Place::getId)
                        .collect(Collectors.toList())
        );

        return bookingResponse;
    }

}
