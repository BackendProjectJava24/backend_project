package com.java24.ajar.models;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Document(collection = "places")
public class Place {
    @Id
    private String id;
    private String name;
    private String description;
    private String[] address = new String[3];
    private ArrayList<String> imageURL = new ArrayList<>();
    private int capacity;
    private int bedroom;
    private double price;
    private ArrayList<AvailabilityPeriod> availability = new ArrayList<>();
    @DBRef
    private User ownerID;
    private String placeType;

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public Place() {
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

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public ArrayList<String> getImageURL() {
        return imageURL;
    }

    public void setImageURL(ArrayList<String> imageURL) {
        this.imageURL = imageURL;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    public ArrayList<AvailabilityPeriod> getAvailability() {
        return availability;
    }

    public void setAvailability(ArrayList<AvailabilityPeriod> availability) {
        this.availability = availability;
    }

    public User getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(User ownerID) {
        this.ownerID = ownerID;
    }




}
