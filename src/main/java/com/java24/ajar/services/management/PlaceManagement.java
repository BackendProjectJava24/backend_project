package com.java24.ajar.services.management;

import com.java24.ajar.checkDate.CheckDate;
import com.java24.ajar.models.Place;
import com.java24.ajar.models.TimePeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceManagement extends CheckDate {

    public PlaceManagement() {}

    //
    protected Place updatePlaceBookedList(Place place, LocalDate checkInDate, LocalDate checkOutDate) {
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
        return place;
    }




    // This method check if the incheckd and outchecked date are in the range of availability time
    public boolean isAllreadyBooked(Place place, LocalDate startDate, LocalDate endDate) {
        // check if the inchecknig and outchecking dates is in the range of avilability period
        return checkTimePeriod(place.getBookedList(), startDate, endDate);
    }


    //
    public boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
        return checkTimePeriod(place.getAvailability(), startDate, endDate);
    }

    // check the place is not pending in this period
    public boolean isPlacePending(Place place, LocalDate checInDate, LocalDate checkOutDate) {
        return checkTimePeriod(place.getPendinglist(), checInDate, checkOutDate);
    }
}
