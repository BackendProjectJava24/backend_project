package com.java24.ajar.Repositories;

import com.java24.ajar.models.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends MongoRepository<Place, String> {

    // ✅ Fixed: Added Pageable for pagination support
    Page<Place> findByCityAndPriceBetween(
            String city,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );


    List<Place> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // ✅ Removed: `findById(Object getkey)` (MongoRepository already provides findById(String id))
}
