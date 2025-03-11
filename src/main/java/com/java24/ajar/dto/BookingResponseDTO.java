package com.java24.ajar.dto;

import com.java24.ajar.models.Place;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class BookingResponseDTO {
    private String id;
    private String customerId;
    private double totalAmount;
    private List<BookingItemDTO> items;
    private Map<String, String> quantities;
    private String createdAt;

    public BookingResponseDTO(String id, String customerId, double totalAmount, List<BookingItemDTO> items, Map<String, String> quantities, String createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.items = items;
        this.quantities = quantities;
        this.createdAt = createdAt;
    }

    public BookingResponseDTO() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<BookingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<BookingItemDTO> items) {
        this.items = items;
    }

    public Map<String, String> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<String, String> quantities) {
        this.quantities = quantities;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setBookedPlace(Place place) {
    }
    private int guests; // ✅ Ensure this field exists

    public int getGuests() {  // ✅ Now correctly returns the guests count
        return guests;
    }
    public void setGuests(int guests) {
        this.guests = guests;
    }


    public void setCheckInDate(LocalDate checkInDate) {
        this.createdAt = checkInDate.toString();
    }

    public void setNights(int nights) {
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
    }
}
