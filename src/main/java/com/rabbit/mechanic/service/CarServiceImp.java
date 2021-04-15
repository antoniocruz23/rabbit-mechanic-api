package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.converter.CarConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.*;
import com.rabbit.mechanic.exception.car.CarAlreadyExistsException;
import com.rabbit.mechanic.exception.car.CarNotFoundException;
import com.rabbit.mechanic.exception.customer.CustomerNotFoundException;
import com.rabbit.mechanic.persistence.entity.CarEntity;
import com.rabbit.mechanic.persistence.entity.CustomerEntity;
import com.rabbit.mechanic.persistence.repository.CarRepository;
import com.rabbit.mechanic.persistence.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link CarService} implementation
 */
@Service
public class CarServiceImp implements CarService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public CarServiceImp(CarRepository carRepository, CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * @see CarService#createCar(CreateOrUpdateCarDto)
     */
    @Override
    public CarDetailsDto createCar(CreateOrUpdateCarDto createCarDto) throws CarAlreadyExistsException {

        // Build Car Entity
        LOGGER.debug("Creating car - {}", createCarDto);
        CarEntity carEntity = CarConverter.fromCreateOrUpdateCarDtoToCarEntity(createCarDto);

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", createCarDto.getUserId());
        CustomerEntity customerEntity = customerRepository.findById(createCarDto.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", createCarDto.getUserId());
                    throw new CustomerNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        carEntity.setCustomerEntity(customerEntity);

        // Persist car into database
        LOGGER.info("Persisting car into database");
        try {
            LOGGER.info("Saving car on database");
            carRepository.save(carEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated plate - {}", carEntity, sqlException);
            throw new CarAlreadyExistsException(ErrorMessages.CAR_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving car into database {}", carEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert to CarDetailsDto and return created car
        LOGGER.debug("Retrieving created car");
        return CarConverter.fromCarEntityToCarDetailsDto(carEntity);
    }

    /**
     * @see CarService#getCarById(long)
     */
    @Override
    public CarDetailsDto getCarById(long carId) {

        // Get car from database
        LOGGER.debug("Getting car with id {} from database", carId);
        CarEntity carEntity = carRepository.findById(carId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get car with {} from database", carId);
                    throw new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND);
                });

        // Convert to CarDetailsDto and return car
        LOGGER.debug("Retrieving got car");
        return CarConverter.fromCarEntityToCarDetailsDto(carEntity);
    }

    /**
     * @see CarService#getCarsList(int, int)
     */
    @Override
    public Paginated<CarDetailsDto> getCarsList(int page, int size) {

        // Get all cars from database
        LOGGER.debug("Getting all cars from database");
        Page<CarEntity> carsList = null;

        try {
            carsList = carRepository.findAll(PageRequest.of(page, size, Sort.by("carId")));
        } catch (Exception e) {
            LOGGER.error("Failed while getting all cars from database", e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert list items from CarEntity to CarDetailsDto
        LOGGER.debug("Convert list items from CarEntity to CarDetailsDto");
        List<CarDetailsDto> carsListResponse = new ArrayList<>();
        for (CarEntity carEntity : carsList.getContent()) {
            carsListResponse.add(CarConverter.fromCarEntityToCarDetailsDto(carEntity));
        }

        // Build custom paginated object
        Paginated<CarDetailsDto> results = new Paginated<>(
                carsListResponse,
                page,
                carsListResponse.size(),
                carsList.getTotalPages(),
                carsList.getTotalElements());

        // Return list of CarDetailsDto
        return results;
    }

    /**
     * @see CarService#updateCarDetails(long, CreateOrUpdateCarDto)
     */
    @Override
    public CarDetailsDto updateCarDetails(long carId, CreateOrUpdateCarDto carDetails) {

        // Get car from database
        LOGGER.debug("Getting car with id {} from database", carId);
        CarEntity carEntity = carRepository.findById(carId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get car with {} from database", carId);
                    throw new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND);
                });

        // Update data with carDetails received
        carEntity.setBrand(carDetails.getBrand());
        carEntity.setEngineType(carDetails.getEngineType());
        carEntity.setPlate(carDetails.getPlate());

        // Save changes
        LOGGER.info("Saving updates from car with id {}", carId);
        carRepository.save(carEntity);

        // Convert to CarDetailsDto and return updated car
        LOGGER.debug("Retrieving updated car");
        return CarConverter.fromCarEntityToCarDetailsDto(carEntity);
    }

    /**
     * @see CarService#deleteCar(long)
     */
    @Override
    public void deleteCar(long carId) {

        // Get car from database
        LOGGER.debug("Getting car with id {} from database", carId);
        CarEntity carEntity = carRepository.findById(carId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get car with {} from database", carId);
                    throw new CarNotFoundException(ErrorMessages.CAR_NOT_FOUND);
                });

        // Delete car from database
        LOGGER.debug("Deleting car with id {}", carId);
        carRepository.delete(carEntity);
    }
}

