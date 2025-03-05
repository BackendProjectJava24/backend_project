package com.java24.ajar.controllers;


import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.*;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.User;
import com.java24.ajar.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/bookings")
public class bookingController {
    private static final Logger log = LoggerFactory.getLogger(bookingController.class);
    private final BookingService bookingService;
    @Autowired
    private final BookingRepository bookingRepository;
    private UserRepository userRepository;

    public bookingController(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }


    @PostMapping
    public ResponseEntity<BookingResponseDTO> addBooking(@RequestBody BookingDTO booking) {
        BookingResponseDTO saBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(saBooking, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }



    @GetMapping("/allBooking")
    public ResponseEntity<List<Booking>> gettAllBooking() {
        List<Booking> bookings = bookingRepository.findAll();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<BookingResponseDTO> findBookingById(@PathVariable String id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @GetMapping("/getUserBookings/{customerId}")
    public ResponseEntity<List<Booking>> getUserBookingByAdmin(@PathVariable String customerId) {

        List<Booking> bookings = bookingService.getAllBookingsByCustomerId(customerId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    @GetMapping("/getUserBooking")
    public ResponseEntity<List<Booking>> getUserBooking() {
        List<Booking> bookings = bookingService.getBookingsByUser();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
