package com.java24.ajar.services;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.Place;

import java.time.LocalDate;
import java.util.List;


public interface PlaceService {
     Place createPlace(PlaceRequest placeRequest);
    PlaceResponse updatePlace(String id,PlaceRequest placeRequest);
      Place  deletePlace(String placeId);

      List<Place> getAllPlaces();
      PlaceResponse getPlaceById(String placeId);
      List<Place> findAvailablePlaces(LocalDate startDate, LocalDate endDate);
      List<Place> findPlacesByPraceRange(Double minPrice, Double maxPrice);

      List<Place> searchByCity(String city);
}
