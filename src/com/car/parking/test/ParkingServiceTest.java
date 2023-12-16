package com.car.parking.test;


import com.car.parking.dao.ParkingRecordDAO;
import com.car.parking.model.Car;
import com.car.parking.service.ParkingService;
import com.car.parking.service.ParkingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingServiceTest {

    private ParkingService parkingService;

    @BeforeEach
    void setUp() {
        ParkingRecordDAO parkingRecordDAO = new MockParkingRecordDAO();
        parkingService = new ParkingServiceImpl(parkingRecordDAO);
    }

    @Test
    void testParkCar() {
        Car car = new Car("ABC123", null); // Replace null with a valid entry time

        try {
            boolean parked = parkingService.parkCar(car);
            assertTrue(parked, "Car should be parked successfully");
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    @Test
    void testParkCarWhenFull() {
        // Fill up all parking spots
        for (int i = 0; i < 100; i++) {
            Car car = new Car("ABC" + i, null); // Replace null with a valid entry time
            try {
                parkingService.parkCar(car);
            } catch (Exception e) {
                fail("Exception not expected: " + e.getMessage());
            }
        }

        // Try to park one more car (should fail)
        Car car = new Car("XYZ123", null); // Replace null with a valid entry time
        assertThrows(Exception.class, () -> parkingService.parkCar(car),
                "Parking should be full, and an exception should be thrown.");
    }

    @Test
    void testRemoveCar() {
        Car car = new Car("ABC123", null); // Replace null with a valid entry time

        try {
            parkingService.parkCar(car);
            double charge = parkingService.removeCar(car);
            assertTrue(charge >= 0, "Charge should be a non-negative value");
        } catch (Exception e) {
            fail("Exception not expected: " + e.getMessage());
        }
    }

    // Add more test cases as needed

    private static class MockParkingRecordDAO implements ParkingRecordDAO {
        // Implement mock methods for testing
        @Override
        public void saveEntry(Car car) {
            // Do nothing for mock
        }

        @Override
        public void saveExit(Car car, double charge) {
            // Do nothing for mock
        }
    }
}
