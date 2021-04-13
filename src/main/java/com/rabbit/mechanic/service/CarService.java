package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.exception.CarAlreadyExistsException;
import com.rabbit.mechanic.exception.CarNotFoundException;

/**
 * Common interface for car services, provides methods to manage cars
 */
public interface CarService {

    /**
     * Create new car
     * @param carDetails {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} the car created
     * @throws CarAlreadyExistsException when the car already exists
     */
    CarDetailsDto addNewCar(CreateOrUpdateCarDto carDetails) throws CarAlreadyExistsException;

    /**
     * Get car by id
     * @param carId car id we want to get
     * @return {@link CarDetailsDto} the car obtained
     * @throws CarNotFoundException when the car aren't found
     */
    CarDetailsDto getCarById(long carId) throws CarNotFoundException;

    /**
     * Get all cars
     * @return {@link CarDetailsDto} the cars obtained
     */
    Paginated<CarDetailsDto> getAllCars(int page, int size);

    /**
     * Update car details
     * @param carId car id we want to update
     * @param carDetails {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} the car updated
     * @throws CarNotFoundException when the car aren't found
     */
    CarDetailsDto updateCarDetails(long carId, CreateOrUpdateCarDto carDetails) throws CarNotFoundException;

    /**
     * Delete Car
     * @param carId car id we want to delete
     * @throws CarNotFoundException when the car aren't found
     */
    void deleteCar(long carId) throws CarNotFoundException;
}