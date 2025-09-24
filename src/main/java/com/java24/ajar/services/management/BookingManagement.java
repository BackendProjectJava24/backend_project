package com.java24.ajar.services.management;

import com.java24.ajar.checkDate.CheckDate;
import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;

import java.time.LocalDate;

public class BookingManagement extends PlaceManagement {
    public BookingManagement(){
    }




    public Booking saveBookingInDatabase(BookingDTO bookingDTO, User user, Place place) {
        // create  anew booking
        Booking booking = new Booking();
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setCustomer(user);
        booking.setPlace(place);
        booking.setGuests(bookingDTO.getGuests());
        booking.setNights(bookingDTO.calculateNights());
        booking.setTotalAmount(bookingDTO.calculateTotalAmount(place));
        return booking;
    }
}
