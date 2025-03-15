package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;
import com.java24.ajar.Repositories.UserRepository;

import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.exceptions.UnauthorizedException;
import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, PlaceRepository placeRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
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


    @Override
    public Booking createBooking(BookingDTO bookingDTO) {
        User user = getCurrentAuthenticatedUser();

        Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        // Validate dates against availability
        if (!isPlaceAvailable(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new IllegalArgumentException("Place is not available for selected dates");
        }

      placeRepository.save(updatPlaceAvailability(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));


        // Validate guests count
        if (bookingDTO.getGuests() > place.getGestt()) {
            throw new IllegalArgumentException("Number of guests exceeds place capacity");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // Calculate total amount
        double totalAmount = nights * place.getPrice();

        Booking newBooking = new Booking();
        newBooking.setCustomer(user);
        newBooking.setPlace(place);
        newBooking.setCheckInDate(bookingDTO.getCheckInDate());
        newBooking.setCheckOutDate(bookingDTO.getCheckOutDate());
        newBooking.setGuests(bookingDTO.getGuests());
        newBooking.setTotalAmount(totalAmount);
        LocalDate creationDate = LocalDate.now();
        newBooking.setCreatedAt(creationDate);

        return bookingRepository.save(newBooking);
    }

    // get only the definded information
    private BookingResponseDTO convertBookingToBookingDTO(Booking booking) {
        BookingResponseDTO bookingResponseDTO = new BookingResponseDTO();

        bookingResponseDTO.setCustomer(booking.getCustomer());
        bookingResponseDTO.setBookedPlace(booking.getPlace());
        bookingResponseDTO.setChickInDate(booking.getCheckInDate());
        bookingResponseDTO.setChickOutDate(booking.getCheckOutDate());

        // count the nights between the check in date and check out date
        Period period = Period.between(booking.getCheckInDate(), booking.getCheckOutDate());
        long nights =  period.getDays();
        bookingResponseDTO.setNights((int) nights);

        bookingResponseDTO.setGests(bookingResponseDTO.getGests());
        bookingResponseDTO.setTotalAmount(booking.getTotalAmount());
        bookingResponseDTO.setCreatedAt(booking.getCreatedAt());

        return bookingResponseDTO;
    }

    // the result is true all the time i need more help her to define the problem
    private boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        List<AvailabilityPeriod> existingPeriods = place.getAvailability();
        boolean isAvailable = false;
        for (AvailabilityPeriod existingPeriod : existingPeriods) {
            LocalDate startDateAvailable = existingPeriod.getStartDate();
            LocalDate endDateAvailable = existingPeriod.getEndDate();
            if ((startDate.isEqual(startDateAvailable) || startDate.isAfter(startDateAvailable) && startDate.isBefore(endDate))
                    &&(endDate.isEqual(endDateAvailable) || endDate.isBefore(endDateAvailable)) ) {
                //  overlaps found
               isAvailable = true;  // the place is not avaible
            }
        }
        //  overlaps found
        return isAvailable; // Place is available
    }

    @Override
    public List<Booking> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();


        return bookings;
    }

    @Override
    public List<Booking> getAllBookingsByCustomerId(String customerId) {
List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
if (bookings.isEmpty() || bookings == null) {
    throw new NoSuchElementException("User is not authenticated");
}


        return bookings;
    }

    @Override
    public Booking getBookingById(String id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Booking not found"));

        return booking;
    }

    @Override
    public Booking updateBooking(String id, BookingDTO bookingDTO) {
        User user = getCurrentAuthenticatedUser();

        Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));
        // check if the user want change the place then he has to cancel this booking and do a new bookin by the new place
        Booking bookingToUpdate = bookingRepository.findById(id).orElse(null);
        if (!bookingToUpdate.getPlace().getId().equals(place.getId())) {
            throw new IllegalArgumentException("Places do not match. Please cancel the booking and do a new bookin by the new place.");
        }
//         add the existted booked period to place avilability before edit it.
    Place updatedPlace =  updatePlaceAvailability(place, id);

        // Validate dates against availability
        if (!isPlaceAvailable(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new IllegalArgumentException("Place is not available for selected dates");
        }

        placeRepository.save(updatPlaceAvailability(updatedPlace, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));

        // Validate guests count
        if (bookingDTO.getGuests() > place.getGestt()) {
            throw new IllegalArgumentException("Number of guests exceeds place capacity");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // Calculate total amount
        double totalAmount = nights * place.getPrice();

//        Booking bookingToUpdate = new Booking();
        bookingToUpdate.setCustomer(user);
        bookingToUpdate.setPlace(place);
        bookingToUpdate.setCheckInDate(bookingDTO.getCheckInDate());
        bookingToUpdate.setCheckOutDate(bookingDTO.getCheckOutDate());
        bookingToUpdate.setGuests(bookingDTO.getGuests());
        bookingToUpdate.setTotalAmount(totalAmount);
        LocalDate creationDate = LocalDate.now();
        bookingToUpdate.setCreatedAt(creationDate);

        return bookingRepository.save(bookingToUpdate);

    }
    // this method is use in update and delete method and id return the the booked period to availability period.
        private Place updatePlaceAvailability(Place place,String id) {
            Booking booking = bookingRepository.findById(id).orElse(null);
            if (booking != null) {
                List<AvailabilityPeriod> existingPeriods = place.getAvailability();
                AvailabilityPeriod existingPeriod = new AvailabilityPeriod();
                existingPeriod.setStartDate(booking.getCheckInDate());
                existingPeriod.setEndDate(booking.getCheckOutDate());
                    existingPeriods.add(existingPeriod);
                place.setAvailability(existingPeriods);
            }
        return place;
    }

    @Override
    public String cancelBooking(String id) {
        User user = getCurrentAuthenticatedUser();
        Booking cencelBooking = bookingRepository.findById(id).orElse(null);
        if (cencelBooking == null) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        // update the place availability before the canceling
        placeRepository.save(updatePlaceAvailability(cencelBooking.getPlace(), id));

        if (!cencelBooking.getCustomer().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }
        bookingRepository.delete(cencelBooking);
        return "Booking has been cancelled";
    }

    @Override
    public List<Booking> getUserBookings() {
        User user = getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByCustomer(user);
        if (bookings.isEmpty() || bookings == null) {
            throw new NoSuchElementException("User is not authenticated");
        }
        return bookings;
    }

    // this mehod do the update on the avilability list and is is used in create a new booking method
    private Place updatPlaceAvailability(Place place, LocalDate startDate, LocalDate endDate) {
        // create a list to add the availability period before booking period and another after booking period
        List<AvailabilityPeriod> newAvailabilityList = place.getAvailability();
        // check if the booking poerid is included  in the availability period
        List<AvailabilityPeriod> oldAvailabilityList = place.getAvailability();
        AvailabilityPeriod availabilityPeriodToRemove = new AvailabilityPeriod();
        AvailabilityPeriod availabilityPeriod1 = new AvailabilityPeriod();
        AvailabilityPeriod availabilityPeriod2 = new AvailabilityPeriod();
        for (AvailabilityPeriod oldAvailability : oldAvailabilityList) {

            LocalDate startDateAvailable = oldAvailability.getStartDate();
            LocalDate endDateAvailable = oldAvailability.getEndDate();
            if ((startDate.isEqual(startDateAvailable) || startDate.isAfter(startDateAvailable) && startDate.isBefore(endDate))
                    &&(endDate.isEqual(endDateAvailable) || endDate.isBefore(endDateAvailable)) ) {
                // the avilability period before booking period
                if (!oldAvailability.getStartDate().isEqual(startDate)) {
                    availabilityPeriod1.setStartDate(oldAvailability.getStartDate());
                    availabilityPeriod1.setEndDate(startDate.minus(1, ChronoUnit.DAYS));
                }else {
                    availabilityPeriod1.setStartDate(startDate);
                    availabilityPeriod1.setEndDate(startDate);
                }
                // the avilability period after booking period
                availabilityPeriod2.setStartDate(endDate);
                availabilityPeriod2.setEndDate(oldAvailability.getEndDate());
                availabilityPeriodToRemove = oldAvailability;
            }

        }
        // remove the old avallability period
        newAvailabilityList.remove(availabilityPeriodToRemove);

        // check if the start date is not after the end date
            newAvailabilityList.add(availabilityPeriod1);
            newAvailabilityList.add(availabilityPeriod2);

        // update the place details
        place.setAvailability(newAvailabilityList);
        return place;
    }
}
