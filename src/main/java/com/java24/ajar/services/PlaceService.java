package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService implements PlaceServiceImp {
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceService(PlaceRepository placeRepository, UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Place createPlace(PlaceRequest placeRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Place newPlace = new Place();
        newPlace.setName(placeRequest.getName());
        newPlace.setDescription(placeRequest.getDescription());
        newPlace.setAddress(placeRequest.getAddress());
        List<String> images = new ArrayList<>();
        if (placeRequest.getImageUrl() != null) {
            for (String imageUrl : placeRequest.getImageUrl()) {
                images.add(imageUrl);
            }
        }
        newPlace.setImageURL(images);
        newPlace.setCapacity(placeRequest.getCapacity());
        newPlace.setBedroom(placeRequest.getBedrooms());
        newPlace.setPrice(placeRequest.getPrice());
        List<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
        if (placeRequest.getAvailabilityPeriods() != null){
            for (AvailabilityPeriod period : placeRequest.getAvailabilityPeriods()) {
                AvailabilityPeriod newPeriod = new AvailabilityPeriod();
                newPeriod.setStartDate(period.getStartDate());
                newPeriod.setEndDate(period.getEndDate());
                // copy  anather fields
                availabilityPeriods.add(newPeriod);
            }
        }
        newPlace.setAvailability(availabilityPeriods);
        newPlace.setOwnerID(user);
        newPlace.setPlaceType(placeRequest.getPlaceType());
        return placeRepository.save(newPlace);
    }

    @Override
    public Place updatePlace(String id, PlaceRequest placeRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        // confirm if the user do the update by himself
        Place placeToUpdate = placeRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Place not found"));
        if (!placeToUpdate.getOwnerID().getUsername().equals(user.getUsername()) ) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }

        placeToUpdate.setId(id);
        placeToUpdate.setName(placeRequest.getName());
        placeToUpdate.setDescription(placeRequest.getDescription());
        placeToUpdate.setAddress(placeRequest.getAddress());
        List<String> images = new ArrayList<>();
        if (placeRequest.getImageUrl() != null) {
            for (String imageUrl : placeRequest.getImageUrl()) {
                images.add(imageUrl);
            }
        }
        placeToUpdate.setImageURL(images);
        placeToUpdate.setCapacity(placeRequest.getCapacity());
        placeToUpdate.setBedroom(placeRequest.getBedrooms());
        placeToUpdate.setPrice(placeRequest.getPrice());
        List<AvailabilityPeriod> availabilityPeriods = new ArrayList<>();
        if (placeRequest.getAvailabilityPeriods() != null){
            for (AvailabilityPeriod period : placeRequest.getAvailabilityPeriods()) {
                AvailabilityPeriod newPeriod = new AvailabilityPeriod();
                newPeriod.setStartDate(period.getStartDate());
                newPeriod.setEndDate(period.getEndDate());
                //copy  anather fields
                availabilityPeriods.add(newPeriod);
            }
        }
        placeToUpdate.setAvailability(placeRequest.getAvailabilityPeriods());
        placeToUpdate.setOwnerID(user);
        placeToUpdate.setPlaceType(placeRequest.getPlaceType());
        return placeRepository.save(placeToUpdate);
    }

    @Override
    public void deletePlace(String placeID) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        Place placeToDelete = placeRepository.findById(placeID).orElseThrow(() -> new RuntimeException("Place not found"));
        if (!placeToDelete.getOwnerID().getUsername().equals(user.getUsername()) ) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }
        placeRepository.delete(placeToDelete);
    }

    @Override
    public List<Place> getAllPlaces() {
        List<Place> allPlaces = placeRepository.findAll();
        if (allPlaces.isEmpty() || allPlaces == null) {
            throw new RuntimeException("there are no places in the database");
        }
        return allPlaces;
    }

    @Override
    public List<Place> findAvailablePlaces(LocalDate startDate, LocalDate endDate) {
        List<Place> allPlaces = placeRepository.findAll();

        return allPlaces.stream()
                .filter(place -> isPlaceAvailable(place, startDate, endDate))
                .collect(Collectors.toList());
    }
    private boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {

        if (place.getAvailability() == null || place.getAvailability().isEmpty()) {
            return false; // Place has no availability periods defined
        }

        return place.getAvailability().stream()
                .anyMatch(period ->
                        (startDate.isEqual(period.getStartDate()) || startDate.isAfter(period.getStartDate())) &&
                                (endDate.isEqual(period.getEndDate()) || endDate.isBefore(period.getEndDate()))
                );
    }

}
