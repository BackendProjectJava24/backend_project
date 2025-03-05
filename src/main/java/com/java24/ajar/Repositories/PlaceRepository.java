package com.java24.ajar.Repositories;

import com.java24.ajar.models.Place;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaceRepository extends MongoRepository<Place, String> {
}
