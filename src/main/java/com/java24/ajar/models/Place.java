package com.java24.ajar.models;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;


@Document(collection = "places")
public class Place {
    @Id
    private String id;
    @NotNull(message = "please rype the the name of place.")
    @NotEmpty(message = "name field cannot be empty")
    private String name;
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

    private List<String> imageURL;
    private int gestt;
    private int bedroom;
    private double price;
    private List<AvailabilityPeriod> availability;
    @DBRef
    private User ownerID;
    private String placeType;

    // class constucture
    public Place() {
    }


    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public int getGestt() {
        return gestt;
    }

    public void setGestt(int gestt) {
        this.gestt = gestt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setImageURL(List<String> imageURL) {
        this.imageURL = imageURL;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<AvailabilityPeriod> getAvailability() {
        return availability;
    }

    public void setAvailability(List<AvailabilityPeriod> availability) {
        this.availability = availability;
    }

    public User getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(User ownerID) {
        this.ownerID = ownerID;
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

    public List<String> getImageURL() {
        return imageURL;
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
}
