package com.java24.ajar.controllers;

import com.java24.ajar.Repositories.ReviewRepository;
import com.java24.ajar.dto.ReviewRequest;
import com.java24.ajar.dto.ReviewResponse;
import com.java24.ajar.services.ReviewService;
import com.java24.ajar.services.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
@Autowired
    private ReviewServiceImpl reviewServiceImpl;

    @PostMapping("/newReview")
    public ResponseEntity<ReviewResponse> addReview(@RequestBody ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewServiceImpl.createRevdiew(reviewRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.CREATED);
    }

    @GetMapping("/bookingReviews/{id}")
    public ResponseEntity<List<ReviewResponse>> getAllReviews(@PathVariable String id) {
        List<ReviewResponse> reviewResponses = reviewServiceImpl.getReviewByBooking(id);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }
    @GetMapping("userReview/{customer}")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(@PathVariable String customer) {
        List<ReviewResponse> reviewResponses = reviewServiceImpl.getReviewByUser(customer);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }

    @GetMapping("/placeReview/{place}")
    public ResponseEntity<List<ReviewResponse>> getPlaceReviews(@PathVariable String place) {
        List<ReviewResponse> reviewResponses = reviewServiceImpl.getReviewByPlace(place);
        return new ResponseEntity<>(reviewResponses, HttpStatus.OK);
    }
    @PatchMapping("/updateReview/{id}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable String id, @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse reviewResponse = reviewServiceImpl.updateRevdiew(id, reviewRequest);
        return new ResponseEntity<>(reviewResponse, HttpStatus.OK);
    }
    @DeleteMapping("/deleteReview/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable String id) {
        String response = reviewServiceImpl.deleteRevdiew(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
