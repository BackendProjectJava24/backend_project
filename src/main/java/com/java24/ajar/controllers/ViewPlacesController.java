package com.java24.ajar.controllers;

import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceService;
import com.java24.ajar.services.ViewPlaceimp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/viewplaces")
public class ViewPlacesController {
    @Autowired
    private ViewPlaceimp viewPlace;
    @Autowired
    private PlaceService placeService;


    @GetMapping("/getallplaces")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = viewPlace.getPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }

    @GetMapping("/getplacebyid/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable String id) {
        Place place = viewPlace.getPalaceById(id);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }
}
