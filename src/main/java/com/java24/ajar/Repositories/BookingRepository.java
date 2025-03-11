package com.java24.ajar.Repositories;

import com.java24.ajar.models.Booking;
import com.java24.ajar.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByCustomerId(String customer);

    List<Booking> findByCustomer(User user);
}
