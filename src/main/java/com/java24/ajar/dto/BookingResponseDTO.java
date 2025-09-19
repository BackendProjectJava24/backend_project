package com.java24.ajar.dto;

import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;

import java.time.LocalDate;

public class BookingResponseDTO {
    private Place BookedPlace;
    private LocalDate chickInDate;
    private LocalDate chickOutDate;
    private int nights;
    private int guests;
    private double totalAmount;
    private LocalDate createdAt;


    public BookingResponseDTO() {
    }


    public Place getBookedPlace() {
        return BookedPlace;
    }

    public void setBookedPlace(Place bookedPlace) {
        BookedPlace = bookedPlace;
    }

    public LocalDate getChickInDate() {
        return chickInDate;
    }

    public void setChickInDate(LocalDate chickInDate) {
        this.chickInDate = chickInDate;
    }

    public LocalDate getChickOutDate() {
        return chickOutDate;
    }

    public void setChickOutDate(LocalDate chickOutDate) {
        this.chickOutDate = chickOutDate;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
