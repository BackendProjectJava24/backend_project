package com.java24.ajar.services;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponseDTO;
import java.util.List;

public interface BookingServiceImp {
    // Create a booking
    BookingResponseDTO createBooking(BookingDTO bookingDTO);

    // Get all bookings
    List<BookingResponseDTO> getAllBookings();

    // Get all bookings for a specific customer
    List<BookingResponseDTO> getAllBookingsByCustomerId(String customerId);

    // Get a single booking by ID
    BookingResponseDTO getBookingById(String id);

    // Update a booking
    BookingResponseDTO updateBooking(String id, BookingDTO bookingDTO);
}
