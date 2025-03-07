package com.java24.ajar.dto;

import com.java24.ajar.models.Address;
import com.java24.ajar.models.AvailabilityPeriod;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;


import java.util.ArrayList;
import java.util.List;

public class PlaceResponse {
    private String placeName;
    private String description;

    // address information this whould help me to filtring the places by the address info and the price range.
    private  String steet;
    private  String postalCode;
    private  String city;
    private  String country;
    private double latitude;
    private double longitude;


    private List<String> imageUrl ;
    private int gestt;
    private  int bedrooms;
    private double price;
    private List<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
    private String placeType;


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



    public List<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<String> imageUrl) {
        this.imageUrl = imageUrl;
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

    public List<AvailabilityPeriod> getAvailabilityPeriods() {
        return availabilityPeriods;
    }

    public void setAvailabilityPeriods(List<AvailabilityPeriod> availabilityPeriods) {
        this.availabilityPeriods = availabilityPeriods;
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

    public int getGestt() {
        return gestt;
    }

    public void setGestt(int gestt) {
        this.gestt = gestt;
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

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
}
