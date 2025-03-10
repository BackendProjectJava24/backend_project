package com.java24.ajar.controllers;

import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.Place;
import com.java24.ajar.services.PlaceServiceImp;
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
    private PlaceServiceImp placeServiceImp;

    @PostMapping("/newplace")
    public ResponseEntity<PlaceResponse> addNewPlace(@RequestBody PlaceRequest placeRequest) {
        PlaceResponse placeResponse = placeServiceImp.createPlace(placeRequest);
        return new ResponseEntity<>(placeResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeServiceImp.getAllPlaces();
        return new ResponseEntity<>(places, HttpStatus.OK);
    }



    @PatchMapping("/updateplace/{id}")
    public ResponseEntity<PlaceResponse> updatePlace(@PathVariable String id, @RequestBody PlaceRequest placeRequest) {
        PlaceResponse place = placeServiceImp.updatePlace(id, placeRequest);
        return new ResponseEntity<>(place, HttpStatus.OK);
    }


@DeleteMapping("/deleteplace/{id}")
    public ResponseEntity<Place> deletePlace(@PathVariable String id) {
        placeServiceImp.deletePlace(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
}

    @GetMapping("/availablePlaces")
    public List<Place> findAvailablePlaces(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return placeServiceImp.findAvailablePlaces(startDate, endDate);
    }

}
