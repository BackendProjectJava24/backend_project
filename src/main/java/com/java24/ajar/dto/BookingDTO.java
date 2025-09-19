package com.java24.ajar.dto;

import com.java24.ajar.models.Place;
import com.java24.ajar.models.TimePeriod;

import java.time.LocalDate;
import java.time.Period;

public class BookingDTO {
        private String placeId;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private int guests;

    public BookingDTO() {
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public int calculateNights() {
        return Period.between(checkInDate, checkOutDate).getDays();
    }

    public double calculateTotalAmount(Place place) {
        double price = place.getPrice();
        double totalDiscount = place.getDiscount() * price;
        int nights = calculateNights();
        return  + nights * price - totalDiscount ;
    }
}
