package com.java24.ajar.Repositories;

import com.java24.ajar.models.Place;
import com.java24.ajar.models.Reviow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Reviow, String> {
    List<Reviow> findAllByBooking(String bookingId);

    List<Reviow> findAllByCustomer(String customer);

    List<Reviow> findAllByPlace(String place);
}
