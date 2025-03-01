package com.java24.ajar.services;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponse;
import com.java24.ajar.dto.BookingResponseDTO;


import java.util.List;

public interface BookingServiceImp {
     BookingResponseDTO createBooking(BookingDTO bookingDTO);
     List<BookingResponse> getAllBookings();
     // get all user's booking
    List<BookingResponseDTO> getAllBookingsByCustomerId();
// get a booking by id
     BookingResponseDTO getBookingById(String id);
}
