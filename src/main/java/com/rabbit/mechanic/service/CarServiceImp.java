package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.converter.CarConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.CarAlreadyExistsException;
import com.rabbit.mechanic.exception.CarNotFoundException;
import com.rabbit.mechanic.exception.DataBaseCommunicationException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.persistence.entity.CarEntity;
import com.rabbit.mechanic.persistence.entity.UserEntity;
import com.rabbit.mechanic.persistence.repository.CarRepository;
import com.rabbit.mechanic.persistence.repository.UserRepository;
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

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarServiceImp(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    /**
     * @see CarService#addNewCar(CreateOrUpdateCarDto)
     */
    @Override
    public CarDetailsDto addNewCar(CreateOrUpdateCarDto createOrUpdateCarDto) throws CarAlreadyExistsException {

        // Build Car Entity
        LOGGER.debug("Creating car - {}", createOrUpdateCarDto);
        CarEntity carEntity = CarConverter.fromCreateOrUpdateCarDtoToCarEntity(createOrUpdateCarDto);

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", createOrUpdateCarDto.getUserId());
        UserEntity userEntity = userRepository.findById(createOrUpdateCarDto.getUserId())
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", createOrUpdateCarDto.getUserId());
                    throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        carEntity.setUserEntity(userEntity);

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

        // Convert to CarDetailsDto and return
        LOGGER.debug("Retrieving got car");
        return CarConverter.fromCarEntityToCarDetailsDto(carEntity);
    }

    /**
     * @see CarService#getAllCars(int, int)
     */
    @Override
    public Paginated<CarDetailsDto> getAllCars(int page, int size) {

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

        //Build custom paginated object
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

