package com.java24.ajar.dto;


import com.java24.ajar.models.AvailabilityPeriod;
import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotNull;


import java.util.ArrayList;
import java.util.List;

public class PlaceResponse {
    @NotNull(message = "please rype the the name of place.")
    private String placeName;
    private String description;

    // address information this whould help me to filtring the places by the address info and the price range.
    @NotNull(message = "please write the addres pf place.")
    private  String street;
    @NotNull(message = "please write the addres pf place.")
    private  String postalCode;
    @NotNull(message = "please write the addres pf place.")
    private  String city;
    @NotNull(message = "please write the addres pf place.")
    private  String country;
    @NotNull(message = "please write the addres pf place.")
    private double latitude;
    @NotNull(message = "please write the addres pf place.")
    private double longitude;


    private List<String> imageUrl ;
    @NegativeOrZero(message = "gues cannot be negative or zero. Please write right guest")
    private int guest;
    @NegativeOrZero(message = "bedroom cannot be negative or zero. Please write right bedroom")
    private  int bedrooms;
    @NegativeOrZero(message = "plice cannot be negative or zero. Please write right price")
    private double price;
    @NotNull(message = "add avalilability period")
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
        return street;
    }

    public void setSteet(String steet) {
        this.street = steet;
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

    public int getGuest() {
        return guest;
    }

    public void setGuest(int guest) {
        this.guest = guest;
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
