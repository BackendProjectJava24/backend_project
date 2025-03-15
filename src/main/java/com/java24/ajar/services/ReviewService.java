package com.java24.ajar.services;

import com.java24.ajar.dto.ReviewResponse;
import com.java24.ajar.dto.ReviewRequest;

import java.util.List;

public interface ReviewService {
    ReviewResponse createRevdiew(ReviewRequest reviewRequest);
    List<ReviewResponse> getReviewByBooking(String bookingId);
    List<ReviewResponse> getReviewByUser(String UserId);
    List<ReviewResponse> getReviewByPlace(String placeId);
    ReviewResponse updateRevdiew(String id, ReviewRequest reviewRequest);
    String deleteRevdiew(String  id);
}
