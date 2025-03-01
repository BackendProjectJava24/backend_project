package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingItemDTO;
import com.java24.ajar.dto.BookingResponse;
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
    public List<BookingResponseDTO> getAllBookingsByCustomerId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Booking> bookings =  bookingRepository.findByCustomerId(user.getId());
        if (bookings.isEmpty()) {
            throw new UnauthorizedException("User has not any bookings");
        }
        List<BookingResponseDTO> bookingResponseDTOS = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingResponseDTOS.add(converToBookingResponseDTO1(booking));
        }
        return bookingResponseDTOS;
    }

    @Override
    public BookingResponseDTO getBookingById(String id) {
        Booking booking = bookingRepository.findById(id).orElse(null);
        if (booking == null) {
            throw new UnauthorizedException("User is not any booking");
        }
         return   converToBookingResponseDTO1(booking);
    }


    @Override
    public BookingResponseDTO createBooking(BookingDTO bookingDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        List<Place> places = new ArrayList<>();
        Map<String, Integer> quantities = new HashMap<>();
        double totalAmount = 0.0;

        for (BookingItemDTO itemDTO : bookingDTO.getItems()) {
            Place place = placeRepository.findById(itemDTO.getPlaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Place not found"));
            places.add(place);
            if (places.isEmpty()){
                throw new UnauthorizedException("there are no places in this booking");
            }
        }
        Booking newBooking = new Booking();
        newBooking.setCustomer(user);
        newBooking.setItems(places);
        newBooking.setTotalAmount(totalAmount);
        newBooking.setQuantities(quantities);

        Booking savedBooking = bookingRepository.save(newBooking);
        return  converToBookingResponseDTO1(savedBooking);
    }








    public BookingResponseDTO converToBookingResponseDTO1(Booking booking) {
        BookingResponseDTO bookingResponseDTO  =  new BookingResponseDTO();
        bookingResponseDTO.setId(booking.getId());
        bookingResponseDTO.setCustomerId(booking.getCustomer().getId());
        List<Place> places = booking.getItems();
        List<BookingItemDTO> bookingItemDTOS = new ArrayList<>();
        for (Place place : places) {
            BookingItemDTO bookingItemDTO = new BookingItemDTO();
            bookingItemDTO.setName(place.getName());
            bookingItemDTO.setPrice(place.getPrice());
            bookingItemDTO.setQuantity(place.getCapacity());
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
