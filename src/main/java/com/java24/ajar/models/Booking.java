package com.java24.ajar.models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "bookings")
public class Booking {
@Id
    private String id;

@DBRef
    private User customer;
    private Double totalAmount;
@DBRef
private List<Place> items;
private AvailabilityPeriod  bookingPeriod;
private Map<String, Integer> quantities;
    @CreatedDate
    private Date createdAt;

    public Booking() {
    }

    public AvailabilityPeriod getBookingPeriod() {
        return bookingPeriod;
    }

    public void setBookingPeriod(AvailabilityPeriod bookingPeriod) {
        this.bookingPeriod = bookingPeriod;
    }

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

    public List<Place> getItems() {
        return items;
    }

    public void setItems(List<Place> items) {
        this.items = items;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Map<String, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<String, Integer> quantities) {
        this.quantities = quantities;
    }

}
