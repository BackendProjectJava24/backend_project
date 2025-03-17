package com.java24.ajar.dto;

import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;



public class ReviewRequest {
    private String title;
    private String description;
    private int rating;
    private User customer;
    private Booking booking;
    private Place place;

    public ReviewRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
