package com.java24.ajar.services;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponse;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.models.Booking;


import java.util.List;

public interface BookingServiceImp {
     BookingResponseDTO createBooking(BookingDTO bookingDTO);
     List<Booking> getAllBookings();
     // get all user's booking
    List<Booking> getAllBookingsByCustomerId(String customerId);
// get a booking by id
Booking getBookingById(String id);

BookingResponseDTO updateBooking(String id, BookingDTO bookingDTO);

String cancelBooking(String id);

// the user can se all his bookings
    List<Booking> getUserBookings();
}
