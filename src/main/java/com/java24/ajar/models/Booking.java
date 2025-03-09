package com.java24.ajar.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @DBRef
    private User customer;

    @DBRef
    private Place place;  // Single place, not a list

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int guests;  // Number of people staying
    private Double totalAmount;  // Calculated based on nights × price

    @CreatedDate
    private Date createdAt;  // MongoDB will auto-set this

    private List<Place> items;  // Fixed: Now declared
    private Map<String, Integer> quantities;  // Fixed: Now declared

    // ✅ Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
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

    }

    public void setTotalAmount(double totalAmount) {
    }

    public void setQuantities(Map<String, Integer> quantities) {

    }

    public void setItems(List<Place> places) {

    }

    public List<Place> getItems() {
                    return null;
    }

    public <K> Map<K, Integer> getQuantities() {
            return null;
    }

    public double getTotalAmount() {
            return 0;
    }

    public Object getCreatedAt() {
        return null;
    }
}
