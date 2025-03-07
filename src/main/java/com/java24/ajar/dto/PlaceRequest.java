package com.java24.ajar.dto;

import com.java24.ajar.models.AvailabilityPeriod;

import java.util.ArrayList;
import java.util.List;

public class PlaceRequest {
        private String name;
        private String description;

        // address information this whould help me to filtring the places by the address info and the price range.
        private  String steet;
        private  String postalCode;
        private  String city;
        private  String country;
    private double latitude;
    private double longitude;


    private List<String> imageUrl;
        private int gest;
        private  int bedrooms;
        private double price;
        private ArrayList<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
        private String placeType;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getGest() {
        return gest;
    }

    public void setGest(int gest) {
        this.gest = gest;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailabilityPeriods(ArrayList<AvailabilityPeriod> availabilityPeriods) {
        this.availabilityPeriods = availabilityPeriods;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getSteet() {
        return steet;
    }

    public void setSteet(String steet) {
        this.steet = steet;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }




}
