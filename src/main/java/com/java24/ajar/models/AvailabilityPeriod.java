package com.java24.ajar.models;

import javax.sound.sampled.FloatControl;
import java.time.LocalDate;

public class AvailabilityPeriod {
    private LocalDate startDate;
    private LocalDate endDate;

    public AvailabilityPeriod() {
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
