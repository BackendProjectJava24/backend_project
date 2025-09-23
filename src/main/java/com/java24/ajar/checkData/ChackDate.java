package com.java24.ajar.checkData;

import com.java24.ajar.models.AvailabilityPeriod;
import com.java24.ajar.models.Place;

import java.time.LocalDate;
import java.util.List;



public class ChackDate {
    public ChackDate() {
    }



    // the result is true all the time i need more help her to define the problem
    public boolean isPlaceAvailable(Place place, LocalDate startDate, LocalDate endDate) {
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
}
