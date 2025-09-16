package com.java24.ajar.controllers;

import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceService;
import com.java24.ajar.services.ViewPlaceimp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/availablePlaces")
    public List<Place> findAvailablePlaces(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return placeService.findAvailablePlaces(startDate, endDate);
    }

    @GetMapping("/searchByCity/{city}")
    public ResponseEntity<List<Place>> findByCity(@PathVariable String city) {
        List<Place> cityPlaces = placeService.searchByCity(city);
        return new ResponseEntity<>(cityPlaces, HttpStatus.OK);
    }


    @GetMapping("/findPlacesByPriceRange")
    public ResponseEntity<List<Place>> findPlacesByPriceRange(@RequestParam double minPrice ,@RequestParam double maxPrice ) {
        List<Place> findedplaces = placeService.findPlacesByPriceRange(minPrice, maxPrice);
        return new ResponseEntity<>(findedplaces, HttpStatus.OK);
    }


    @GetMapping("/getplacesbyowner")
    public ResponseEntity<List<Place>> getPlacesByOwner() {
        List<Place> places = placeService.getPlacesByOwner();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }
}
