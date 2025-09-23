package com.java24.ajar.services.management;

import com.java24.ajar.models.Place;
import com.java24.ajar.models.TimePeriod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceManagement {

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

}
