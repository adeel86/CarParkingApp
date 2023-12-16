package com.car.parking.service;

import com.car.parking.model.Car;

public interface ParkingService {
    boolean parkCar(Car car) throws Exception;
    double removeCar(Car car) throws Exception;
}
