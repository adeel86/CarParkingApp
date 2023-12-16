package com.car.parking.model;

import java.time.LocalDateTime;

public class Car {
    private final String licensePlate;
    private final LocalDateTime entryTime;

    public Car(String licensePlate, LocalDateTime entryTime) {
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
    }

    public String getLicensePlate() {

        return licensePlate;
    }

    public LocalDateTime getEntryTime() {

        return entryTime;
    }
}
