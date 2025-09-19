package com.java24.ajar.services;

import com.java24.ajar.Repositories.BookingRepository;
import com.java24.ajar.Repositories.PlaceRepository;

import com.java24.ajar.config.CheckAuthentiction;
import com.java24.ajar.dto.BookingDTO;
import com.java24.ajar.dto.BookingResponseDTO;
import com.java24.ajar.models.TimePeriod;
import com.java24.ajar.models.Booking;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingService implements BookingServiceImp {
    @Autowired
    private final BookingRepository bookingRepository;
    private final PlaceRepository placeRepository;
    @Autowired
    CheckAuthentiction checkAuthentication;
    @Autowired
    private PlaceService placeService;

    public BookingService(BookingRepository bookingRepository, PlaceRepository placeRepository) {
        this.bookingRepository = bookingRepository;
        this.placeRepository = placeRepository;
    }


    @Override
    public Booking createBooking(BookingDTO bookingDTO) {
        User user = checkAuthentication.getCurrentAuthenticatedUser();

        Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

        // Validate dates against availability
        if (!isPlaceAvailable(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new IllegalArgumentException("Place is not available for selected dates");
        }

      placeRepository.save(updatePlaceAvailability(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));


        // Validate guests count
        if (bookingDTO.getGuests() > place.getGuest()) {
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

        bookingResponseDTO.setBookedPlace(booking.getPlace());
        bookingResponseDTO.setChickInDate(booking.getCheckInDate());
        bookingResponseDTO.setChickOutDate(booking.getCheckOutDate());

        bookingResponseDTO.setNights(booking.getNights());
        bookingResponseDTO.setGuests(bookingResponseDTO.getGuests());
        bookingResponseDTO.setTotalAmount(booking.getTotalAmount());
        bookingResponseDTO.setCreatedAt(booking.getCreatedAt());

        return bookingResponseDTO;
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
        User user = checkAuthentication.getCurrentAuthenticatedUser();

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

        placeRepository.save(updatePlaceAvailability(updatedPlace, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate()));

        // Validate guests count
        if (bookingDTO.getGuests() > place.getGuest()) {
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
                List<TimePeriod> existingPeriods = place.getAvailability();
                TimePeriod existingPeriod = new TimePeriod();
                existingPeriod.setStartDate(booking.getCheckInDate());
                existingPeriod.setEndDate(booking.getCheckOutDate());
                    existingPeriods.add(existingPeriod);
                place.setAvailability(existingPeriods);
            }
        return place;
    }

    @Override
    public String cancelBooking(String id) {
        User user = checkAuthentication.getCurrentAuthenticatedUser();
        Booking cancelbooking = bookingRepository.findById(id).orElse(null);
        if (cancelbooking == null) {
            throw new IllegalArgumentException("User is not authenticated");
        }
        // update the place availability before the canceling
        placeRepository.save(updatePlaceAvailability(cancelbooking.getPlace(), id));

        if (!cancelbooking.getCustomer().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("You cant delete this place. You are not owner of this place");
        }
        bookingRepository.delete(cancelbooking);
        return "Booking has been cancelled";
    }

    @Override
    public List<Booking> getUserBookings() {
        User user = checkAuthentication.getCurrentAuthenticatedUser();
        List<Booking> bookings = bookingRepository.findByCustomer(user);
        if (bookings.isEmpty() || bookings == null) {
            throw new NoSuchElementException("User is not authenticated");
        }
        return bookings;
    }

    // this mehod do the update on the avilability list and is is used in create a new booking method
    private Place updatePlaceAvailability(Place place, LocalDate startDate, LocalDate endDate) {
        // create a list to add the availability period before booking period and another after booking period
        List<TimePeriod> newAvailabilityList = place.getAvailability();
        // check if the booking poerid is included  in the availability period
        List<TimePeriod> oldAvailabilityList = place.getAvailability();
        TimePeriod availabilityPeriodToRemove = new TimePeriod();
        TimePeriod availabilityPeriod1 = new TimePeriod();
        TimePeriod availabilityPeriod2 = new TimePeriod();
        for (TimePeriod oldAvailability : oldAvailabilityList) {

            LocalDate startDateAvailable = oldAvailability.getStartDate();
            LocalDate endDateAvailable = oldAvailability.getEndDate();
            if ((startDate.isEqual(startDateAvailable) || startDate.isAfter(startDateAvailable) && startDate.isBefore(endDate))
                    &&(endDate.isEqual(endDateAvailable) || endDate.isBefore(endDateAvailable)) ) {
                // the avilability period before booking period
                availabilityPeriod1.setStartDate(oldAvailability.getStartDate());
                availabilityPeriod1.setEndDate(startDate    .minus(1, ChronoUnit.DAYS));

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




    public BookingResponseDTO makeBooking(BookingDTO bookingDTO) {
        // check if the user is authenticated
        User user = checkAuthentication.getCurrentAuthenticatedUser();

        //check if all the nacessary data to do a booking in added
        //validateDataBooking(bookingDTO);

        // check if the period the use added, it is avilable.
         Place place = placeRepository.findById(bookingDTO.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Place not found"));

         //
        if(!isPlaceAvailable(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())){
           throw new IllegalArgumentException("the place is not available");
        }


        if (isAllreadyBooked(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())){
            throw new IllegalArgumentException("the place is already booked");
        }

        // check the placce status, the place is not  pended or allready booked
        /*
        if (isPlacePending(place, bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate())) {
            throw new IllegalStateException("the place is already pending");
        }

         */
        Booking booking = new Booking();

        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setCustomer(user);
        booking.setPlace(place);
        booking.setGuests(bookingDTO.getGuests());
        booking.setNights(bookingDTO.calculateNights());
        booking.setTotalAmount(bookingDTO.calculateTotalAmount(place));

        // update the list which inclues the booked period for the place
        updatePlaceBookedList(place,  bookingDTO.getCheckInDate(), bookingDTO.getCheckOutDate());

        // save the new booking in the database
        bookingRepository.save(booking);


        return convertBookingToBookingDTO(booking);
    }


    private BookingDTO validateDataBooking(BookingDTO bookingDTO) {
        if(bookingDTO.getCheckInDate() == null   || bookingDTO.getCheckOutDate() == null ) {
            throw new  IllegalArgumentException("Check In Date and Check Out Date cannot be empty");
        }

        return bookingDTO;
    }
    //


    //
    private boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        return checkTimePeriod(place.getAvailability(), startDate, endDate);
    }

    // check the place is not pending in this period
    private boolean  isPlacePending(Place place, LocalDate checInDate, LocalDate checkOutDate)  {
     return checkTimePeriod(place.getPendinglist(), checInDate, checkOutDate);
    }
    //
    private void updatePlaceBookedList(Place place, LocalDate checkInDate, LocalDate checkOutDate) {
        TimePeriod bookedPeriod = new TimePeriod();
        bookedPeriod.setStartDate(checkInDate);
        bookedPeriod.setEndDate(checkOutDate);

        // check if the booked list is existed or.
        if (place.getBookedList() == null) {
            List<TimePeriod> newBookedList = new ArrayList<TimePeriod>();
            newBookedList.add(bookedPeriod);
            place.setBookedList(newBookedList);
        }else{
            List<TimePeriod> updatedBookedList = place.getBookedList();
            updatedBookedList.add(bookedPeriod);
            place.setBookedList(updatedBookedList);
        }
        placeRepository.save(place);
    }

    private boolean isAllreadyBooked(Place place, LocalDate startDate, LocalDate endDate) {
        // check if the inchecknig and outchecking dates is in the range of avilability period
        return checkTimePeriod(place.getBookedList(), startDate, endDate);
    }
    // this method is used to check of a date is existed between to dates.
    private boolean checkTimePeriod(List<TimePeriod> existingPeriods, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isAlreadyBooked = false;
        for (TimePeriod existingPeriod : existingPeriods) {
            if (checkInDate.isAfter(existingPeriod.getStartDate()) && checkInDate.isBefore(existingPeriod.getEndDate())) {
                isAlreadyBooked = true;
            }
            if (checkOutDate.isBefore(existingPeriod.getEndDate()) && checkOutDate.isAfter(existingPeriod.getStartDate())) {
                isAlreadyBooked = true;
            }
            if (checkInDate.isEqual(existingPeriod.getStartDate()) || checkInDate.isEqual(existingPeriod.getEndDate())
                    || checkOutDate.isEqual(existingPeriod.getStartDate()) || checkOutDate.isEqual(existingPeriod.getEndDate())) {
                isAlreadyBooked = true;
            }
        }
        //  overlaps found
        return isAlreadyBooked; // Place is available
    }
}
