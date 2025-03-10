package com.java24.ajar.models;

public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;
    private Double latitude;
    private Double longitude;

    public Address() {
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }



    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}

