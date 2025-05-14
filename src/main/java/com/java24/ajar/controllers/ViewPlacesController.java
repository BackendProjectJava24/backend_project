package com.java24.ajar.controllers;

import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceService;
import com.java24.ajar.services.ViewPlaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/viewplaces")
public class ViewPlacesController {
    @Autowired
    private ViewPlaces viewPlaces;
    @Autowired
    private PlaceService placeService;


    @GetMapping("/getallplaces")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = viewPlaces.getPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @GetMapping("/getplacebyid/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable String id) {
        Place place = viewPlaces.getPalaceById(id);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }
}
