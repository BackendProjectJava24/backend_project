package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PöaceService implements PlaceServiceImp {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PöaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Place createPlace(PlaceRequest placeRequest) {
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if(auth == null || !auth.isAuthenticated()) {
    throw  new RuntimeException("User is not authenticated");
}
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
User user = userRepository.findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

Place newPlace = new Place();
        newPlace.setName(placeRequest.getPlaceName());
        newPlace.setDescription(placeRequest.getDescription());
        newPlace.setAddress(placeRequest.getAddress());
        newPlace.setImageURL(placeRequest.getImageUrl());
        newPlace.setCapacity(placeRequest.getCapacity());
        newPlace.setBedroom(placeRequest.getBedrooms());
        newPlace.setPrice(placeRequest.getPrice());
        newPlace.setAvailability(placeRequest.getAvailabilityPeriods());
        newPlace.setOwnerID(user);
        newPlace.setPlaceType(placeRequest.getPlaceType());
return  placeRepository.save(newPlace);
    }
}
