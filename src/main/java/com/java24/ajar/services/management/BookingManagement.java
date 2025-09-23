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
    CheckDate checkDate = new CheckDate();


    // This method check if the incheckd and outchecked date are in the range of availability time
    public boolean isAllreadyBooked(Place place, LocalDate startDate, LocalDate endDate) {
        // check if the inchecknig and outchecking dates is in the range of avilability period
        return checkDate.checkTimePeriod(place.getBookedList(), startDate, endDate);
    }


    //
    public boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        return checkDate.checkTimePeriod(place.getAvailability(), startDate, endDate);
    }

    // check the place is not pending in this period
    public boolean isPlacePending(Place place, LocalDate checInDate, LocalDate checkOutDate) {
        return checkDate.checkTimePeriod(place.getPendinglist(), checInDate, checkOutDate);
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
