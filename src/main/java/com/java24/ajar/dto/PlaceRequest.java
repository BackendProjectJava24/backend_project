package com.java24.ajar.dto;

import com.java24.ajar.models.AvailabilityPeriod;

import java.util.ArrayList;

public class PlaceRequest {
    private String placeName;
    private String description;
    private String[] address = new String[2];
    private ArrayList<String> imageUrl = new ArrayList<>();
    private int capacity;
    private  int bedrooms;
    private double price;
    private ArrayList<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
    private String placeType;

    public PlaceRequest() {
    }

    public PlaceRequest(String placeName, String description, String[] address, ArrayList<String> imageUrl, int capacity, int bedrooms, double price, ArrayList<AvailabilityPeriod> availabilityPeriods, String placeType) {
        this.placeName = placeName;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.bedrooms = bedrooms;
        this.price = price;
        this.availabilityPeriods = availabilityPeriods;
        this.placeType = placeType;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ArrayList<AvailabilityPeriod> getAvailabilityPeriods() {
        return availabilityPeriods;
    }

    public void setAvailabilityPeriods(ArrayList<AvailabilityPeriod> availabilityPeriods) {
        this.availabilityPeriods = availabilityPeriods;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
}
