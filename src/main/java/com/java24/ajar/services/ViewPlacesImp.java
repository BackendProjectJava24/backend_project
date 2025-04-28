package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.models.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ViewPlacesImp implements ViewPlaces {
    @Autowired
    private PlaceRepository placeRepository;
    @Override
    public List<Place> getPlaces() {
        List <Place> places = placeRepository.findAll();
        return places;
    }
}
