package com.car.parking;

import com.car.parking.dao.ParkingRecordDAO;
import com.car.parking.dao.ParkingRecordDAOImpl;
import com.car.parking.model.Car;
import com.car.parking.service.ParkingService;
import com.car.parking.service.ParkingServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParkingApp {

    public static void main(String[] args) {
        ParkingRecordDAO parkingRecordDAO = new ParkingRecordDAOImpl();
        ParkingService parkingService = new ParkingServiceImpl(parkingRecordDAO);

        // List to store CompletableFuture for asynchronous operations
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        // Create 5 cars for parking
        List<Car> cars = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Car car = new Car("ABC" + i, LocalDateTime.now()); // Use the current time for simplicity
            cars.add(car);

            // Perform parking asynchronously
            CompletableFuture<Void> parkFuture = CompletableFuture.runAsync(() -> {
                try {
                    parkingService.parkCar(car);
                    System.out.println("Car " + car.getLicensePlate() + " parked successfully.");
                } catch (Exception e) {
                    System.out.println("Error parking car " + car.getLicensePlate() + ": " + e.getMessage());
                }
            });
            futures.add(parkFuture);
        }

        // Wait for all parking operations to complete
        CompletableFuture<Void> allOfPark = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        try {
            allOfPark.get(); // Wait for all parking operations to complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // List to store CompletableFuture for asynchronous car removal operations
        List<CompletableFuture<Void>> removalFutures = new ArrayList<>();

        // Perform car removal asynchronously
        for (Car car : cars) {
            CompletableFuture<Void> removalFuture = CompletableFuture.runAsync(() -> {
                try {
                    double charge = parkingService.removeCar(car);
                    System.out.println("Car " + car.getLicensePlate() + " removed. Charge: £" + charge);
                } catch (Exception e) {
                    System.out.println("Error removing car " + car.getLicensePlate() + ": " + e.getMessage());
                }
            });
            removalFutures.add(removalFuture);
        }

        // Wait for all car removal operations to complete
        CompletableFuture<Void> allOfRemoval = CompletableFuture.allOf(removalFutures.toArray(new CompletableFuture[0]));
        try {
            allOfRemoval.get(); // Wait for all car removal operations to complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
