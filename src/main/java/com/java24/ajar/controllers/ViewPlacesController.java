package com.java24.ajar.controllers;

import com.java24.ajar.models.Place;
import com.java24.ajar.services.ViewPlaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/viewplaces")
public class ViewPlacesController {
    @Autowired
    private ViewPlaces viewPlaces;



    @GetMapping("/getallplaces")
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = viewPlaces.getPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }
}
