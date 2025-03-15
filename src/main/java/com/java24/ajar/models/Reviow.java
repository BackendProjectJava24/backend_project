package com.java24.ajar.models;


import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

// The Reviews table is added to store customer feedback.
@Document(collection = "reviews")

public class Reviow {
    @Id
    private String id;
    private String title;
    //Text of the review.
    private String description;
    // The rating field is an integer, so you could easily calculate average ratings.
  // Rating given by the customer (e.g., 1 to 5 stars).
   @Positive
    private int rating;
    // The review_date field is a datetime to store the exact time the review was submitted.
private LocalDate date;
   //  referencing Bookings): Booking associated with the review.
   @DBRef
    private Booking booking;
    @DBRef
    //referencing Customers): Customer who wrote the reviewf
    private User customer;
    //  referencing Resources: Resource being reviewed.
    @DBRef
    private Place place;

    public Reviow() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public @Positive int getRating() {
        return rating;
    }

    public void setRating(@Positive int rating) {
        this.rating = rating;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
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
}
