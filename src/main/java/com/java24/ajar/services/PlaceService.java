package com.java24.ajar.services;

import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.config.CheckAuthentiction;
import com.java24.ajar.dto.PlaceRequest;
import com.java24.ajar.dto.PlaceResponse;
import com.java24.ajar.models.TimePeriod;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
    @Autowired
  CheckAuthentiction  checkAuthentication;


    // this method handle the place adding to database
    @Override
    public Place createPlace(PlaceRequest placeRequest) {
        User user = checkAuthentication.getCurrentAuthenticatedUser();
        Place newPlace = new Place();
        newPlace.setName(placeRequest.getName());
        newPlace.setDescription(placeRequest.getDescription());

        // add the address information to database
        // chek the address field are not null

      //  validatePlace(placeRequest);
        newPlace.setStreet(placeRequest.getStreet());
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
        newPlace.setGuest(placeRequest.getGest());
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
        placeResponse.setSteet(place.getStreet());
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

        placeResponse.setSteet(place.getStreet());
        placeResponse.setBedrooms(place.getBedroom());
        placeResponse.setPrice(place.getPrice());
        placeResponse.setPlaceType(place.getPlaceType());
        List<TimePeriod> availabilityPeriods = place.getAvailability();
        if (availabilityPeriods != null) {
            placeResponse.setAvailabilityPeriods(availabilityPeriods);
        }
        return placeResponse;
    }

    @Override
    public PlaceResponse updatePlace(String id, PlaceRequest placeRequest) {
        // check if the user is authenticated.
        User user = checkAuthentication.getCurrentAuthenticatedUser();
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
        validatePlace(placeRequest);

        placeToUpdate.setStreet(placeRequest.getStreet());

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
        placeToUpdate.setGuest(placeRequest.getGest());
        placeToUpdate.setBedroom(placeRequest.getBedrooms());
        placeToUpdate.setPrice(placeRequest.getPrice());
        placeToUpdate.setOwnerID(user);
        placeToUpdate.setAvailability(validateAvailabilityPeriod(placeRequest.getAvailabilityPeriods()));
        placeToUpdate.setPlaceType(placeRequest.getPlaceType());

        // update the place
        placeRepository.save(placeToUpdate);
        return convertToPlaceResponse(placeToUpdate);
    }

    @Override
    public Place deletePlace(String placeID) {
        User user = checkAuthentication.getCurrentAuthenticatedUser();
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
        // endate don't have before start date
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("startDate and endDate cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate cannot be after endDate");
        }
        List<Place> places = placeRepository.findAll();

        if (places == null || places.isEmpty()) {
            throw new IllegalArgumentException("there are no places in the database");
        }
        List<Place> tempPlaces = new ArrayList<>();
        for (Place place : places) {
            if (isPlaceAvailable(place, startDate, endDate)) {
                tempPlaces.add(place);
            }
        }
        places.clear();
        places.addAll(tempPlaces);
        if (tempPlaces.isEmpty()) {
            throw new IllegalArgumentException("there are no places that match in this period.");
        }
        return places;
    }


    @Override
    public List<Place> findPlacesByPriceRange(Double minPrice, Double maxPrice) {
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

    @Override
    public List<Place> getPlacesByOwner() {
        User user = checkAuthentication.getCurrentAuthenticatedUser();
        String ownerID = user.getId();
        List<Place> allPlaces = placeRepository.findAllByOwnerID(ownerID);
        if (allPlaces.isEmpty() || allPlaces == null) {
            throw new IllegalArgumentException("there are no places in the database");
        }
        return allPlaces;
    }

    // This method is linked to  findAvailablePlaces method.
    private boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        List<TimePeriod> existingPeriods = place.getAvailability();
        boolean isAvailable = false;
        for (TimePeriod existingPeriod : existingPeriods) {
            LocalDate startDateAvailable = existingPeriod.getStartDate();
            LocalDate endDateAvailable = existingPeriod.getEndDate();
            if ((startDate.isEqual(startDateAvailable) || startDate.isAfter(startDateAvailable) && startDate.isBefore(endDate))
                    && (endDate.isEqual(endDateAvailable) || endDate.isBefore(endDateAvailable))) {
                //  overlaps found
                isAvailable = true;  // the place is not avaible
            }
        }
        //  overlaps found
        return isAvailable; // Place is available
    }

    private void validatePlace(PlaceRequest placeRequest) {
        if (placeRequest.getName() == null || placeRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Place name cannot be empty");
        }
        if (placeRequest.getCity() == null || placeRequest.getCity().isEmpty()) {
            throw new IllegalArgumentException("Place city is required");
        }
        if (placeRequest.getCountry().isEmpty() || placeRequest.getCountry() == null) {
            throw new IllegalArgumentException("Place country is required");
        }
        if (placeRequest.getPostalCode().isEmpty() || placeRequest.getPostalCode() == null) {
            throw new IllegalArgumentException("Place postal code is required");
        }
        if (placeRequest.getStreet() == null || placeRequest.getStreet().isEmpty()) {
            throw new IllegalArgumentException("Place street is required");
        }
        if (placeRequest.getGest() < 0){
            throw new IllegalArgumentException("Place guest cannot be negative or zero");
        }
        if (placeRequest.getBedrooms() < 0){
            throw new IllegalArgumentException("Place bedrooms cannot be required or zero");
        }
        if (placeRequest.getPrice() <= 0){
            throw new IllegalArgumentException("Place price cannot be negative or zero");
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

    private List<TimePeriod> validateAvailabilityPeriod(List<TimePeriod> availabilityPeriod) {

        List<TimePeriod> newPeriods = new ArrayList<>();
        for (TimePeriod period : availabilityPeriod) {
            if (period.getStartDate().isAfter(period.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            if (period.getStartDate().isAfter(period.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be before end date");
            }
            if (period.getStartDate().isBefore(LocalDate.now()) || period.getEndDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Start date or end date cannot be in the pass");
            }
            for (TimePeriod newPeriod : newPeriods) {
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
