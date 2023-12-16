package com.car.parking.service;

import com.car.parking.dao.ParkingRecordDAO;
import com.car.parking.exception.ParkingException;
import com.car.parking.model.Car;
import com.car.parking.model.ParkingSpot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class ParkingServiceImpl implements ParkingService {
    private static final int PARKING_SIZE = 100;
    private final ParkingSpot[] parkingSpots;
    private final ParkingRecordDAO parkingRecordDAO;

    public ParkingServiceImpl(ParkingRecordDAO parkingRecordDAO) {
        this.parkingRecordDAO = parkingRecordDAO;
        this.parkingSpots = new ParkingSpot[PARKING_SIZE];
        for (int i = 0; i < PARKING_SIZE; i++) {
            parkingSpots[i] = new ParkingSpot(i + 1);
        }
    }

    @Override
    public boolean parkCar(Car car) throws ParkingException {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isOccupied()) {
                spot.occupy();
                parkingRecordDAO.saveEntry(car);
                return true;
            }
        }
        throw new ParkingException("Parking is full. Cannot park the car.");
    }

    @Override
    public double removeCar(Car car) throws ParkingException {
        CompletableFuture<Double> chargeFuture = calculateChargeAsync(car);
        double charge;
        try {
            charge = chargeFuture.get();
        } catch (Exception e) {
            throw new ParkingException("Error calculating charge: " + e.getMessage());
        }

        for (ParkingSpot spot : parkingSpots) {
            if (spot.isOccupied() && car.getLicensePlate().equals(car.getLicensePlate())) {
                spot.vacate();
                parkingRecordDAO.saveExit(car, charge);
                return charge;
            }
        }

        throw new ParkingException("Car not found in the parking lot.");
    }

    private CompletableFuture<Double> calculateChargeAsync(Car car) {
        return CompletableFuture.supplyAsync(() -> {
            // Implement charge calculation logic
            // For simplicity, charge is fixed at £2 per hour
            Duration duration = Duration.between(car.getEntryTime(), LocalDateTime.now());
            long hours = duration.toHours();
            return hours * 2.0;
        });
    }
}
