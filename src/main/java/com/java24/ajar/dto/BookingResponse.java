package com.java24.ajar.dto;

import java.util.List;

public class BookingResponse {
    private String userId;
    private List<String> bookingPlaceIds;

    public BookingResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getBookingPlaceIds() {
        return bookingPlaceIds;
    }

    public void setBookingPlaceIds(List<String> bookingPlaceIds) {
        this.bookingPlaceIds = bookingPlaceIds;
    }
}
