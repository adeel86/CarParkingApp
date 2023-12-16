package com.car.parking.model;

public class ParkingSpot {
    private final int spotNumber;
    private boolean occupied;

    public ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.occupied = false;
    }

    public int getSpotNumber() {

        return spotNumber;
    }

    public boolean isOccupied() {

        return occupied;
    }

    public void occupy() {

        this.occupied = true;
    }

    public void vacate() {

        this.occupied = false;
    }
}
