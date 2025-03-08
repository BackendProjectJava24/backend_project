package com.java24.ajar.dto;

import com.java24.ajar.models.User;
import java.time.LocalDate;
import java.util.List;

public class BookingDTO {
    private String placeId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;
    private User customerId;
    private List<BookingItemDTO> items;

    // Default Constructor
    public BookingDTO() {}

    // Parameterized Constructor
    public BookingDTO(String placeId, LocalDate checkInDate, LocalDate checkOutDate, int guests, User customerId, List<BookingItemDTO> items) {
        this.placeId = placeId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guests = guests;
        this.customerId = customerId;
        this.items = items;
    }

    // Getters and Setters
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

    public User getCustomerId() {
        return customerId;
    }

    public void setCustomerId(User customerId) {
        this.customerId = customerId;
    }

    public List<BookingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BookingItemDTO> items) {
        this.items = items;
    }
}
