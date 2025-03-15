package com.java24.ajar.controllers;


import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.*;
import com.java24.ajar.models.Booking;
import com.java24.ajar.services.BookingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/bookings")
public class BookingController {
    private static final Logger log = LoggerFactory.getLogger(BookingController.class);
    private final BookingServiceImpl bookingService;
    @Autowired
    private final BookingRepository bookingRepository;
    private UserRepository userRepository;

    public BookingController(BookingServiceImpl bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }


    @PostMapping
    public ResponseEntity<Booking> addBooking(@RequestBody BookingDTO booking) {
        Booking saBooking = bookingService.createBooking(booking);
        return new ResponseEntity<>(saBooking, HttpStatus.CREATED);
    }


    @GetMapping
    @PreAuthorize("hasRole(ADMIN)")
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }



    @GetMapping("/allBooking")
    public ResponseEntity<List<Booking>> gettAllBooking() {
        List<Booking> bookings = bookingService.getAllBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    @GetMapping("/findById/{id}")
    public ResponseEntity<Booking> findBookingById(@PathVariable String id) {
        Booking booking = bookingService.getBookingById(id);
        return new ResponseEntity<>(booking, HttpStatus.OK);
    }

    @GetMapping("/getUserBookings/{customerId}")
    public ResponseEntity<List<Booking>> getUserBookingByAdmin(@PathVariable String customerId) {
        List<Booking> bookings = bookingService.getAllBookingsByCustomerId(customerId);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
    @GetMapping("/getUserBooking")
    public ResponseEntity<List<Booking>> getUserBooking() {
        List<Booking> bookings = bookingService.getUserBookings();
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @DeleteMapping("/deleteBooking/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable String id) {
        String message = bookingService.cancelBooking(id);
        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/updateBooking/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable String id,  @RequestBody BookingDTO booking) {
        Booking bookingResponseDTO = bookingService.updateBooking(id, booking);
        return new ResponseEntity<>(bookingResponseDTO, HttpStatus.CREATED);
    }
}
