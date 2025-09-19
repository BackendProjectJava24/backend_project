package com.java24.ajar.models;

import java.time.LocalDate;

public class TimePeriod {
    private LocalDate startDate;
    private LocalDate endDate;

    public TimePeriod() {
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
