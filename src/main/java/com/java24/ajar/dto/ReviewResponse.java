package com.java24.ajar.dto;

import com.java24.ajar.models.Place;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class ReviewResponse {
    private String title;
    private String description;
    private Place place;
    private int rating;

    public ReviewResponse() {
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
