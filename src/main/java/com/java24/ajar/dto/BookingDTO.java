package com.java24.ajar.dto;

import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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

    public double calculateNights(LocalDate checkInDate, LocalDate checkOutDate) {
        return Period.between(checkInDate, checkOutDate).getDays();
    }
}
