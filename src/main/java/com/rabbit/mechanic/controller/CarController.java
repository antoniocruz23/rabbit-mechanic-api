package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.service.CarServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Car Controller who provides endpoints
 */
@RestController
@RequestMapping("/cars")
public class CarController {

    private static final Logger LOGGER = LogManager.getLogger(CarController.class);
    private final CarServiceImp carService;

    public CarController(CarServiceImp carService) {
        this.carService = carService;
    }

    /**
     * Create new Car
     * @param createOrUpdateCarDto {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} car created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<CarDetailsDto> createCar(@Valid @RequestBody CreateOrUpdateCarDto createOrUpdateCarDto) {

        LOGGER.info("Request to create - {}.", createOrUpdateCarDto);
        CarDetailsDto carDetails = carService.addNewCar(createOrUpdateCarDto);

        LOGGER.info("Service retrieved created car {}", carDetails);
        return new ResponseEntity<>(carDetails, HttpStatus.CREATED);
    }

    /**
     * Get car by id
     * @param carId car id we want to get
     * @return {@link CarDetailsDto} the car wanted and Ok httpStatus
     */
    @GetMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> getCarById(@PathVariable long carId) {

        LOGGER.info("Request to get car with id {}", carId);
        CarDetailsDto carDetails = carService.getCarById(carId);

        LOGGER.info("Service retrieved to got user {}", carDetails);
        return new ResponseEntity<>(carDetails, HttpStatus.OK);
    }

    /**
     * Get all cars
     * @return {@link CarDetailsDto} list of all cars and Ok httpStatus
     */
    @GetMapping
    public ResponseEntity<Paginated<CarDetailsDto>> getCarsList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {

        LOGGER.info("Request to get all cars");
        Paginated<CarDetailsDto> carsList = carService.getAllCars(page, size);

        LOGGER.info("Service retrieved to got cars {}", carsList);
        return new ResponseEntity<>(carsList, HttpStatus.OK);
    }

    /**
     * Update Car
     * @param carId car id we want to update
     * @param createOrUpdateCarDto {@link CreateOrUpdateCarDto}
     * @return {@link CarDetailsDto} car updated and Ok httpStatus
     */
    @PutMapping("/{carId}")
    public ResponseEntity<CarDetailsDto> updateCar(@PathVariable long carId,
                                                    @Valid @RequestBody CreateOrUpdateCarDto createOrUpdateCarDto) {

        LOGGER.info("Request to update car with id {}", carId);
        CarDetailsDto carDetailsDto = carService.updateCarDetails(carId, createOrUpdateCarDto);

        LOGGER.info("Service retrieved to update car {}", carDetailsDto);
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
        carService.deleteCar(carId);

        LOGGER.info("Car with id {} deleted successfully", carId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
