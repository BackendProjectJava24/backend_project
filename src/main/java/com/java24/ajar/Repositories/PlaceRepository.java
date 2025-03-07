package com.java24.ajar.Repositories;

import com.java24.ajar.models.Place;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    List<Place> findByCity(String city);

    //fidn the places than its price between mini pricce and max price
    List<Place> findByPriceBetween(Double minPrice, Double maxPrice);
}
