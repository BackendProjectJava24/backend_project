package com.java24.ajar.services;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.models.Place;
public interface PlaceServiceImp {
    public Place createPlace(PlaceRequest placeRequest);
}
