package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.Address;
import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    // Add the authenticate method
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new UnauthorizedException("User is not authenticated");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public PlaceResponse createPlace(PlaceRequest placeRequest) {
        User user= getCurrentAuthenticatedUser();
        Place newPlace = new Place();
        newPlace.setName(placeRequest.getName());
        newPlace.setDescription(placeRequest.getDescription());
        Address address = validateAddress(placeRequest.getAddress());
        newPlace.setAddress(address);
        List<String> images = new ArrayList<>();
        if (placeRequest.getImageUrl() != null) {
            for (String imageUrl : placeRequest.getImageUrl()) {
                images.add(imageUrl);
            }
        }
        newPlace.setImageURL(images);
        newPlace.setOwnerID(user);
        newPlace.setCapacity(placeRequest.getCapacity());
        newPlace.setBedroom(placeRequest.getBedrooms());
        newPlace.setPrice(placeRequest.getPrice());


        newPlace.setAvailability(validateAvailabilityPeriod(placeRequest.getAvailabilityPeriods()));
        newPlace.setOwnerID(user);
        newPlace.setPlaceType(placeRequest.getPlaceType());

        placeRepository.save(newPlace);


        return convertToPlaceResponse(newPlace);
    }

    private PlaceResponse convertToPlaceResponse(Place place) {
        PlaceResponse placeResponse = new PlaceResponse();
        if (place.getAddress() == null) {
            validateAddress(place.getAddress());
        }
        placeResponse.setAddress(place.getAddress());
        placeResponse.setDescription(place.getDescription());
        placeResponse.setPrice(place.getPrice());
        placeResponse.setPlaceName(place.getName());
        placeResponse.setPlaceType(place.getPlaceType());
        List<AvailabilityPeriod> availabilityPeriods = place.getAvailability();
        if (availabilityPeriods != null) {
            placeResponse.setAvailabilityPeriods(availabilityPeriods);
        }
        placeResponse.setBedrooms(place.getBedroom());
        placeResponse.setPrice(place.getPrice());
        List<String> images = place.getImageURL();
        if (images != null) {
            placeResponse.setImageUrl(images);
        }

        return placeResponse;
    }

    @Override
    public PlaceResponse updatePlace(String id, PlaceRequest placeRequest) {
        getCurrentAuthenticatedUser();
    User user = getCurrentAuthenticatedUser();
        // confirm if the user do the update by himself
        Place placeToUpdate = placeRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Place not found"));
        if (!placeToUpdate.getOwnerID().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }

        placeToUpdate.setId(id);
        placeToUpdate.setName(placeRequest.getName());
        placeToUpdate.setDescription(placeRequest.getDescription());
        placeToUpdate.setAddress(validateAddress(placeRequest.getAddress()));
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
        placeToUpdate.setOwnerID(user);
        placeToUpdate.setAvailability(validateAvailabilityPeriod(placeRequest.getAvailabilityPeriods()));
        placeToUpdate.setOwnerID(user);
        placeToUpdate.setPlaceType(placeRequest.getPlaceType());
        placeRepository.save(placeToUpdate);
        return convertToPlaceResponse(placeToUpdate);
    }

    @Override
    public void deletePlace(String placeID) {
       User user = getCurrentAuthenticatedUser();
        Place placeToDelete = placeRepository.findById(placeID).orElseThrow(() -> new IllegalArgumentException("Place not found"));
        if (!placeToDelete.getOwnerID().getUsername().equals(user.getUsername())) {
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

//        if (allPlaces.isEmpty() || allPlaces == null || allPlaces.isEmpty()) {
//            throw new RuntimeException("there are no places in the database");
//        }
        List<Place> availabledPlaces = new ArrayList<>();
        for (Place place : allPlaces) {
            List<AvailabilityPeriod> availabilityPeriod = place.getAvailability();
            for (AvailabilityPeriod period : availabilityPeriod) {
                if (startDate.isEqual(period.getStartDate()) || startDate.isAfter(period.getStartDate())
                        ||  endDate.isEqual(period.getEndDate()) || endDate.isBefore(period.getEndDate())) {
                    availabledPlaces.add(place);
                }
            }
        }
//        if (availabledPlaces.isEmpty() || availabledPlaces == null) {
//            throw new RuntimeException("there are no places in the database");
//        }
        return availabledPlaces;


//        return allPlaces.stream()
//                .filter(place -> isPlaceAvailable(place, startDate, endDate))
//                .collect(Collectors.toList());


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


    private Address validateAddress(Address address) {

        if (address.getCity() == null || address.getCity().isEmpty()) {
            address.setCity("No City is limited");
        }
        if (address.getCountry().isEmpty() || address.getCountry() == null) {
            address.setCountry("No Country is limited");
        }
        if (address.getPostalCode().isEmpty() || address.getPostalCode() == null) {
            address.setPostalCode("No PostalCode is limited");
        }
        if (address.getLatitude() == null) {
            address.setLatitude(0.0);
        }
        if (address.getLongitude() == null) {
            address.setLongitude(0.0);
        }
        return address;
    }
    public List<Place> searchByCity(String city) {
        List<Place> allPlaces = placeRepository.findAll();
        List<Place> searchedPlaces = new ArrayList<>();
        for (Place place : allPlaces) {
            if (place.getAddress().getCity().equals(city)) {
                searchedPlaces.add(place);
            }
        }
        if (searchedPlaces.isEmpty()) {
            throw new RuntimeException("There are no places in the database");
        }
        return searchedPlaces;
    }

    private List<AvailabilityPeriod> validateAvailabilityPeriod(List<AvailabilityPeriod> availabilityPeriod ) {

        List<AvailabilityPeriod> newPeriods = new ArrayList<>();
        for (AvailabilityPeriod period : availabilityPeriod) {
            if (period.getStartDate().isAfter(period.getEndDate())){
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            if (period.getStartDate().isAfter(period.getEndDate())){
                throw new IllegalArgumentException("Start date cannot be before end date");
            }
            if (period.getStartDate().isBefore(LocalDate.now()) || period.getEndDate().isBefore(LocalDate.now())){
                throw new IllegalArgumentException("Start date or end date cannot be in the pass");
            }
            for (AvailabilityPeriod newPeriod : newPeriods) {
                if (newPeriod.getStartDate().isAfter(period.getStartDate()) || period.getStartDate().isEqual(newPeriod.getStartDate()) || period.getStartDate().isBefore(newPeriod.getEndDate()) || period.getStartDate().isEqual(newPeriod.getEndDate())
                        && newPeriod.getEndDate().isBefore(period.getEndDate()) || period.getEndDate().isEqual(newPeriod.getEndDate())) {
                    throw new IllegalArgumentException("you have already added this period");
                }

            }
            newPeriods.add(period);
        }
        return newPeriods;
    }
}
