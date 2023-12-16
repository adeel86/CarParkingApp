package com.car.parking.dao;

import com.car.parking.model.Car;

public interface ParkingRecordDAO {
    void saveEntry(Car car);
    void saveExit(Car car, double charge);
}
