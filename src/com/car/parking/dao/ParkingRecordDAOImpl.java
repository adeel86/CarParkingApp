package com.car.parking.dao;

import com.car.parking.model.Car;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingRecordDAOImpl implements ParkingRecordDAO {
    private final Map<String, LocalDateTime> entryRecords = new HashMap<>();

    @Override
    public void saveEntry(Car car) {

        entryRecords.put(car.getLicensePlate(), car.getEntryTime());
    }

    @Override
    public void saveExit(Car car, double charge) {
        LocalDateTime entryTime = entryRecords.remove(car.getLicensePlate());
        Duration duration = Duration.between(entryTime, LocalDateTime.now());
        long hours = duration.toHours();
        double totalCharge = hours * 2.0;

        if (totalCharge != charge) {
            throw new RuntimeException("Charge calculation error.");
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("parking_records.txt", true))) {
            writer.println("License Plate: " + car.getLicensePlate());
            writer.println("Entry Time: " + entryTime);
            writer.println("Exit Time: " + LocalDateTime.now());
            writer.println("Duration (hours): " + hours);
            writer.println("Charge: £" + charge);
            writer.println("------------------------");
        } catch (IOException e) {
            throw new RuntimeException("Error writing to file: " + e.getMessage());
        }
    }
}
