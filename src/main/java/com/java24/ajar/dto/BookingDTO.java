package com.java24.ajar.dto;

import com.java24.ajar.models.User;

import java.util.List;

public class BookingDTO {
    private User customerId;
    private List<BookingItemDTO> items;

    public BookingDTO() {
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
