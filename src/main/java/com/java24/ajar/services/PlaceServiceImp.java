package com.java24.ajar.services;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.Place;

import javax.sound.sampled.FloatControl;
import java.time.LocalDate;
import java.util.List;


public interface PlaceServiceImp {
     PlaceResponse createPlace(PlaceRequest placeRequest);

    PlaceResponse updatePlace(String id,PlaceRequest placeRequest);
      void deletePlace(String placeId);

      List<Place> getAllPlaces();
      List<Place> findAvailablePlaces(LocalDate startDate, LocalDate endDate);
}
