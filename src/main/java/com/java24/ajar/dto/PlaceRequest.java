package com.java24.ajar.dto;

import com.java24.ajar.models.AvailabilityPeriod;

import java.util.ArrayList;

public class PlaceRequest {
    private String name;
    private String description;
    private String address;
    private ArrayList<String> imageUrl = new ArrayList<>();
    private int capacity;
    private  int bedrooms;
    private double price;
    private ArrayList<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
    private String placeType;



    public PlaceRequest(String placeName, String description, String address, ArrayList<String> imageUrl, int capacity, int bedrooms, double price, ArrayList<AvailabilityPeriod> availabilityPeriods, String placeType) {
        this.name = placeName;
        this.description = description;
        this.address = address;
        this.imageUrl = imageUrl;
        this.capacity = capacity;
        this.bedrooms = bedrooms;
        this.price = price;
        this.availabilityPeriods = availabilityPeriods;
        this.placeType = placeType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }





    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }



    public int getCapacity() {
        return capacity;
    }



    public int getBedrooms() {
        return bedrooms;
    }



    public double getPrice() {
        return price;
    }



    public ArrayList<AvailabilityPeriod> getAvailabilityPeriods() {
        return availabilityPeriods;
    }


    public String getPlaceType() {
        return placeType;
    }


    public String getAddress() {
        return address;
    }
}
