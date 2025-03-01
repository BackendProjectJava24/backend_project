package com.java24.ajar.dto;

public class BookingItemDTO {

    private String placeId;
    private String name;
    private double price;

    private int quantity;

    public BookingItemDTO() {
    }

    public BookingItemDTO(String placeId, String name, double price, int quantity) {
        this.placeId = placeId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String  placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
