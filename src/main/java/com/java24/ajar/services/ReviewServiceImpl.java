package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.ReviewRepository;
import com.java24.ajar.Repositories.UserRepository;
import com.java24.ajar.dto.ReviewResponse;
import com.java24.ajar.dto.ReviewRequest;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.Reviow;
import com.java24.ajar.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(UserRepository userRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
    }


    @Override
    public ReviewResponse createRevdiew(ReviewRequest reviewRequest) {
        User user = getCurrentAuthenticatedUser();
        // the customer do the revview by himself

        // Ensure the user do review for his booking and a plice he already booked
        Booking booking = bookingRepository.findById(reviewRequest.getBooking().getId()).orElse(null);
        if (booking == null) {
            throw new IllegalArgumentException("You don't have a booking");
        }

        // find the place will the user leave his review for
        Place place = booking.getPlace();
        Reviow review = new Reviow();
        review.setTitle(reviewRequest.getTitle());
        review.setDescription(reviewRequest.getDescription());
        review.setRating(reviewRequest.getRating());
        review.setBooking(booking);
        review.setPlace(place);
        LocalDate localDate = LocalDate.now();
        review.setDate(localDate);
        review.setRating(reviewRequest.getRating());
        review.setCustomer(user);
   reviewRepository.save(review);

return  convertBookingToReviewResponse(review );

    }

    @Override
    public List<ReviewResponse> getReviewByBooking(String bookingId) {
        List<Reviow> reviows = reviewRepository.findAllByBooking(bookingId);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Reviow reviow : reviows) {
            ReviewResponse reviewResponse = convertBookingToReviewResponse(reviow);
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }

    @Override
    public List<ReviewResponse> getReviewByUser(String userId) {
        List<Reviow> reviows = reviewRepository.findAllByCustomer(userId);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Reviow reviow : reviows) {
            ReviewResponse reviewResponse = convertBookingToReviewResponse(reviow);
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }

    @Override
    public List<ReviewResponse> getReviewByPlace(String placeId) {
        List<Reviow> reviows = reviewRepository.findAllByPlace(placeId);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        for (Reviow reviow : reviows) {
            ReviewResponse reviewResponse = convertBookingToReviewResponse(reviow);
            reviewResponses.add(reviewResponse);
        }
        return reviewResponses;
    }


    private ReviewResponse convertBookingToReviewResponse(Reviow review) {
       ReviewResponse reviewResponse = new ReviewResponse();
      reviewResponse.setPlace(review.getPlace());
       reviewResponse.setTitle(review.getTitle());
       reviewResponse.setDescription(review.getDescription());
       reviewResponse.setRating(review.getRating());
       return reviewResponse;
    }


    @Override
    public ReviewResponse updateRevdiew(String id,ReviewRequest reviewRequest) {
        User user = getCurrentAuthenticatedUser();

        Reviow reviewToUpldate = reviewRepository.findById(id).orElse(null);
        if (reviewToUpldate == null) {
            throw new IllegalArgumentException("You don't have a booking");
        }

        if (!user.getId().equals(reviewToUpldate.getCustomer().getId())) {
            throw new IllegalArgumentException("You don't have agree to do this update");
        }
        reviewToUpldate.setRating(reviewRequest.getRating());
        reviewToUpldate.setTitle(reviewRequest.getTitle());
        reviewToUpldate.setDescription(reviewRequest.getDescription());
        return convertBookingToReviewResponse(reviewRepository.save(reviewToUpldate));
    }

    @Override
    public String  deleteRevdiew(String id) {
        Reviow reviewToDelete = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("You don't have a booking"));

        reviewRepository.delete(reviewToDelete);
        return "Deleted review";
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

}
