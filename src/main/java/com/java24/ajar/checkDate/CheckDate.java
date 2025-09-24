package com.java24.ajar.checkDate;

import com.java24.ajar.models.TimePeriod;

import java.time.LocalDate;
import java.util.List;

public class CheckDate {

    // this method is used to check of a date is existed between to dates.
    public boolean checkTimePeriod(List<TimePeriod> existingPeriods, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean IsTrue = false;
        for (TimePeriod existingPeriod : existingPeriods) {
            if (checkInDate.isAfter(existingPeriod.getStartDate()) && checkInDate.isBefore(existingPeriod.getEndDate())) {
                IsTrue = true;
            }
            if (checkOutDate.isBefore(existingPeriod.getEndDate()) && checkOutDate.isAfter(existingPeriod.getStartDate())) {
                IsTrue = true;
            }
            if (checkInDate.isEqual(existingPeriod.getStartDate()) || checkInDate.isEqual(existingPeriod.getEndDate())
                    || checkOutDate.isEqual(existingPeriod.getStartDate()) || checkOutDate.isEqual(existingPeriod.getEndDate())) {
                IsTrue = true;
            }
        }
        return IsTrue; // Place is available
    }
}
