package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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


    // this method handle the place adding to database
    @Override
    public Place createPlace(PlaceRequest placeRequest) {
        User user = getCurrentAuthenticatedUser();
        Place newPlace = new Place();
        newPlace.setName(placeRequest.getName());
        newPlace.setDescription(placeRequest.getDescription());

        // add the addres information to database
        // chek the addres field are not null
        validateAddress(placeRequest);
        newPlace.setSteet(placeRequest.getSteet());
        newPlace.setPostalCode(placeRequest.getPostalCode());
        newPlace.setCity(placeRequest.getCity());
        newPlace.setCountry(placeRequest.getCountry());
        newPlace.setLatitude(placeRequest.getLatitude());
        newPlace.setLongitude(placeRequest.getLongitude());

        List<String> images = new ArrayList<>();
        if (placeRequest.getImageUrl() != null) {
            for (String imageUrl : placeRequest.getImageUrl()) {
                images.add(imageUrl);
            }
        }
        newPlace.setImageURL(images);
        newPlace.setOwnerID(user);
        newPlace.setGestt(placeRequest.getGest());
        newPlace.setBedroom(placeRequest.getBedrooms());
        newPlace.setPrice(placeRequest.getPrice());
        // add the availability periods when the place is available to booking
        newPlace.setAvailability(validateAvailabilityPeriod(placeRequest.getAvailabilityPeriods()));

        newPlace.setOwnerID(user);
        newPlace.setPlaceType(placeRequest.getPlaceType());


        return placeRepository.save(newPlace);
    }

    private PlaceResponse convertToPlaceResponse(Place place) {
        PlaceResponse placeResponse = new PlaceResponse();

        placeResponse.setPlaceName(place.getName());
        placeResponse.setDescription(place.getDescription());

        // view the place information
        // chek the addres field are not null
        placeResponse.setSteet(place.getSteet());
        placeResponse.setPostalCode(place.getPostalCode());
        placeResponse.setCity(place.getCity());
        placeResponse.setCountry(place.getCountry());
        placeResponse.setLatitude(place.getLatitude());
        placeResponse.setLongitude(place.getLongitude());

        // the images of the place
        List<String> images = place.getImageURL();
        if (images != null) {
            placeResponse.setImageUrl(images);
        }

        placeResponse.setSteet(place.getSteet());
        placeResponse.setBedrooms(place.getBedroom());
        placeResponse.setPrice(place.getPrice());
        placeResponse.setPlaceType(place.getPlaceType());
        List<AvailabilityPeriod> availabilityPeriods = place.getAvailability();
        if (availabilityPeriods != null) {
            placeResponse.setAvailabilityPeriods(availabilityPeriods);
        }
        return placeResponse;
    }

    @Override
    public PlaceResponse updatePlace(String id, PlaceRequest placeRequest) {
        // check if the user is authenticated.
        User user = getCurrentAuthenticatedUser();
        // check if the place is existed or not
        Place placeToUpdate = placeRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Place not found"));
        // confirm if the user do the update by himself
        if (!placeToUpdate.getOwnerID().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }

        placeToUpdate.setName(placeRequest.getName());
        placeToUpdate.setDescription(placeRequest.getDescription());
        // address information
        // chek the addres field are not null
        validateAddress(placeRequest);
        placeToUpdate.setSteet(placeRequest.getSteet());
        placeToUpdate.setPostalCode(placeRequest.getPostalCode());
        placeToUpdate.setCity(placeRequest.getCity());
        placeToUpdate.setCountry(placeRequest.getCountry());
        placeToUpdate.setLatitude(placeRequest.getLatitude());
        placeToUpdate.setLongitude(placeRequest.getLongitude());
        // update the place images
        List<String> images = new ArrayList<>();
        if (placeRequest.getImageUrl() != null) {
            for (String imageUrl : placeRequest.getImageUrl()) {
                images.add(imageUrl);
            }
        }
        placeToUpdate.setImageURL(images);
        placeToUpdate.setGestt(placeRequest.getGest());
        placeToUpdate.setBedroom(placeRequest.getBedrooms());
        placeToUpdate.setPrice(placeRequest.getPrice());
        placeToUpdate.setOwnerID(user);
        placeToUpdate.setAvailability(validateAvailabilityPeriod(placeRequest.getAvailabilityPeriods()));
        placeToUpdate.setPlaceType(placeRequest.getPlaceType());

        // update the place
        placeRepository.save(placeToUpdate);
        return  convertToPlaceResponse(placeToUpdate);
    }

    @Override
    public Place deletePlace(String placeID) {
        User user = getCurrentAuthenticatedUser();
        Place placeToDelete = placeRepository.findById(placeID).orElseThrow(() -> new IllegalArgumentException("Place not found"));
        if (!placeToDelete.getOwnerID().getUsername().equals(user.getUsername())) {
            throw new AccessDeniedException("You cant delete this place. You are not owner of this place");
        }
        placeRepository.delete(placeToDelete);
        return placeToDelete;
    }


    // this method help  gelp the admin to find all the places whitch saved in the database
    @Override
    public List<Place> getAllPlaces() {
        List<Place> allPlaces = placeRepository.findAll();
        if (allPlaces.isEmpty() || allPlaces == null) {
            throw new IllegalArgumentException("there are no places in the database");
        }
        return allPlaces;
    }

    @Override
    public PlaceResponse getPlaceById(String placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("Place not found"));
        return convertToPlaceResponse(place);
    }


    //  fing the all availability  places in the database it should help me to do the filter by the different options
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
                        || endDate.isEqual(period.getEndDate()) || endDate.isBefore(period.getEndDate())) {
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

    @Override
    public List<Place> findPlacesByPraceRange(Double minPrice, Double maxPrice) {
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be less than maxPrice");
        }
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("minPrice and maxPrice must be greater than 0");
        }
        List<Place> findedPlacesByPricaRange = placeRepository.findByPriceBetween(minPrice, maxPrice);
        if (findedPlacesByPricaRange.isEmpty()) {
            throw new NoSuchElementException("there are no places in the database");
        }
        return findedPlacesByPricaRange;
    }

    // This method is linked to  findAvailablePlaces method.
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


    private void validateAddress(PlaceRequest placeRequest) {

        if (placeRequest.getCity() == null || placeRequest.getCity().isEmpty()) {
            throw new IllegalArgumentException("Place city is required");
        }
        if (placeRequest.getCountry().isEmpty() || placeRequest.getCountry() == null) {
            throw new IllegalArgumentException("Place country is required");
        }
        if (placeRequest.getPostalCode().isEmpty() || placeRequest.getPostalCode() == null) {
            throw new IllegalArgumentException("Place postal code is required");
        }
        if (placeRequest.getSteet() == null || placeRequest.getSteet().isEmpty()) {
            throw new IllegalArgumentException("Place street is required");
        }
    }

// find all the place in a city
    @Override
    public List<Place> searchByCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("City is required");
        }
        List<Place> cityPlaces = placeRepository.findByCity(city);
        if (cityPlaces.isEmpty() || cityPlaces == null) {
            throw new NoSuchElementException("there are no places in the database");
        }
        return cityPlaces;
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
