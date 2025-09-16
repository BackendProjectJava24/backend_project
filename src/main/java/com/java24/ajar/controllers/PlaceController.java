package com.java24.ajar.controllers;

import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/places")
public class PlaceController {
    @Autowired
    private PlaceService placeService;

    @PostMapping("/newplace")
    public ResponseEntity<Place> addNewPlace(@RequestBody PlaceRequest placeRequest) {
        Place  placeResponse = placeService.createPlace(placeRequest);
        return new ResponseEntity<>(placeResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.getAllPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }
    @GetMapping("/findPlaceById/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable String id) {
        PlaceResponse findedPlace = placeService.getPlaceById(id);
        return new ResponseEntity<>(findedPlace, HttpStatus.OK);
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







    @PatchMapping("/updateplace/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(@Valid @PathVariable String id, @RequestBody PlaceRequest placeRequest) {
        PlaceResponse place = placeService.updatePlace(id, placeRequest);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }


@DeleteMapping("/deleteplace/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable String id) {
       Place placeResponse =  placeService.deletePlace(id);
        return new ResponseEntity<>(placeResponse, HttpStatus.NO_CONTENT);
}




}
