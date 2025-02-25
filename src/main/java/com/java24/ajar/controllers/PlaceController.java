package com.java24.ajar.controllers;

import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    private PlaceServiceImp placeServiceImp;

    @PostMapping("/newplace")
    public ResponseEntity<Place> addNewPlace(@RequestBody PlaceRequest placeRequest) {
        Place place = placeServiceImp.createPlace(placeRequest);
        return new ResponseEntity<>(place, HttpStatus.CREATED);

    }



}
