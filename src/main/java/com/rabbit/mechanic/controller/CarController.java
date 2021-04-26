package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.CarServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Car Controller who provides endpoints
 */
@RestController
@RequestMapping("/api/cars")
@PreAuthorize("@authorized.hasRole(\"RECEPTIONIST\") ||" +
        "@authorized.hasRole(\"ADMIN\")")
public class CarController {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(CarController.class);
    private final CarServiceImp carService;

    public CarController(CarServiceImp carService) {
        this.carService = carService;
    }

    /**
     * Create new Car
     * @param createCarDto {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} car created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<CarDetailsDto> createCar(@Valid @RequestBody CreateOrUpdateCarDto createCarDto) {

        LOGGER.info("Request to create - {}.", createCarDto);
        CarDetailsDto carDetails;
        try {
            carDetails = carService.createCar(createCarDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to created car - {}", createCarDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Car created successfully. Retrieving created car with id {}", carDetails.getCarId());
        return new ResponseEntity<>(carDetails, HttpStatus.CREATED);
    }

    /**
     * Get car by id
     * @param carId car id we want to get
     * @return {@link CarDetailsDto} the car wanted and Ok httpStatus
     */
    @GetMapping("/{carId}")
    @PreAuthorize("@authorized.hasRole(\"RECEPTIONIST\") ||" +
            "@authorized.hasRole(\"MECHANIC\") ||" +
            "@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity<CarDetailsDto> getCarById(@PathVariable long carId) {

        LOGGER.info("Request to get car with id {}", carId);
        CarDetailsDto carDetails;
        try {
            carDetails = carService.getCarById(carId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get car with id {}", carId, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving car with id - {}", carDetails.getCarId());
        return new ResponseEntity<>(carDetails, HttpStatus.OK);
    }

    /**
     * Get cars list
     * @return {@link CarDetailsDto} list of all cars and Ok httpStatus
     */
    @GetMapping
    @PreAuthorize("@authorized.hasRole(\"RECEPTIONIST\") ||" +
                "@authorized.hasRole(\"MECHANIC\") ||" +
            "@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity<Paginated<CarDetailsDto>> getCarsList(@RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "5") int size) {

        LOGGER.info("Request to get cars list - page: {}, size: {}", page, size);
        Paginated<CarDetailsDto> carsList;
        try {
            carsList = carService.getCarsList(page, size);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get cars list", e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving cars list");
        return new ResponseEntity<>(carsList, HttpStatus.OK);
    }

    /**
     * Update Car
     * @param carId car id we want to update
     * @param updateCarDto {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} car updated and Ok httpStatus
     */
    @PutMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> updateCar(@PathVariable long carId,
                                                    @Valid @RequestBody CreateOrUpdateCarDto updateCarDto) {

        LOGGER.info("Request to update car with id {} - {}", carId, updateCarDto);
        CarDetailsDto carDetailsDto;
        try {
            carDetailsDto = carService.updateCarDetails(carId, updateCarDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to update car with id {} - {}", carId, updateCarDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Car with id {} updated successfully. Retrieving updated data", carId);
        return new ResponseEntity<>(carDetailsDto, HttpStatus.OK);
    }

    /**
     * Delete car
     * @param carId car id we want to delete
     * @return Ok httpStatus
     */
    @DeleteMapping("/{carId}")
    public ResponseEntity deleteCar(@PathVariable long carId) {

        LOGGER.info("Request to delete car with id {}", carId);
        try {
            carService.deleteCar(carId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to delete car with id {}", carId, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Car with id {} deleted successfully", carId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
