package com.java24.ajar.services;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponse;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.models.Booking;


import java.util.List;

public interface BookingServiceImp {
     BookingResponseDTO createBooking(BookingDTO bookingDTO);
     List<BookingResponseDTO> getAllBookings();
     // get all user's booking
    List<BookingResponseDTO> getAllBookingsByCustomerId(String customerId);
// get a booking by id
     BookingResponseDTO getBookingById(String id);
}
