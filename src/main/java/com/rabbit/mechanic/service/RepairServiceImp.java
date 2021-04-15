package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.repair.CreateOrUpdateRepairDto;
import com.rabbit.mechanic.command.repair.RepairDetailsDto;
import com.rabbit.mechanic.converter.RepairConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.*;
import com.rabbit.mechanic.exception.customer.CustomerNotFoundException;
import com.rabbit.mechanic.exception.repair.RepairAlreadyExistsException;
import com.rabbit.mechanic.exception.repair.RepairNotFoundException;
import com.rabbit.mechanic.persistence.entity.CarEntity;
import com.rabbit.mechanic.persistence.entity.RepairEntity;
import com.rabbit.mechanic.persistence.repository.CarRepository;
import com.rabbit.mechanic.persistence.repository.RepairRepository;
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
 * An {@link RepairService} implementation
 */
@Service
public class RepairServiceImp implements RepairService {

    private static final Logger LOGGER = LogManager.getLogger(RepairService.class);
    private final RepairRepository repairRepository;
    private final CarRepository carRepository;

    public RepairServiceImp(RepairRepository repairRepository, CarRepository carRepository) {
        this.repairRepository = repairRepository;
        this.carRepository = carRepository;
    }

    /**
     * @see RepairService#createRepair(CreateOrUpdateRepairDto)
     */
    @Override
    public RepairDetailsDto createRepair(CreateOrUpdateRepairDto createRepairDto) throws RepairAlreadyExistsException {

        // Build CustomerEntity
        LOGGER.debug("Creating repair - {}", createRepairDto);
        RepairEntity repairEntity = RepairConverter.fromCreateOrUpdateRepairDtoToRepairEntity(createRepairDto);

        // Get car from database
        LOGGER.debug("Getting car with id {} from database", createRepairDto.getCarId());
        CarEntity carEntity = carRepository.findById(createRepairDto.getCarId())
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get car with {} from database", createRepairDto.getCarId());
                    throw new CustomerNotFoundException(ErrorMessages.CAR_NOT_FOUND);
                });

        repairEntity.setCarEntity(carEntity);

        // Persist repair into database
        LOGGER.info("Persisting repair into database");
        try {
            LOGGER.info("Saving repair on database");
            repairRepository.save(repairEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated name - {}", repairEntity, sqlException);
            throw new RepairAlreadyExistsException(ErrorMessages.REPAIR_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving repair into database {}", repairEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert to RepairDetailsDto and return created repair
        LOGGER.debug("Retrieving created repair");
        return RepairConverter.fromRepairEntityToRepairDetailsDto(repairEntity);
    }

    /**
     * @see RepairService#getRepairById(long)
     */
    @Override
    public RepairDetailsDto getRepairById(long repairId) throws RepairNotFoundException {

        // Get repair from database
        LOGGER.debug("Getting repair with id {} from database", repairId);
        RepairEntity repairEntity = repairRepository.findById(repairId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get repair with {} from database", repairId);
                    throw new RepairNotFoundException(ErrorMessages.REPAIR_NOT_FOUND);
                });

        // Convert to RepairDetailsDto and return repair
        LOGGER.debug("Retrieving got repair");
        return RepairConverter.fromRepairEntityToRepairDetailsDto(repairEntity);
    }

    /**
     * @see RepairService#getRepairsList(int, int)
     */
    @Override
    public Paginated<RepairDetailsDto> getRepairsList(int page, int size) {

        // Get all repairs from database
        LOGGER.debug("Getting all repairs from database");
        Page<RepairEntity> repairList = null;

        try {
            repairList = repairRepository.findAll(PageRequest.of(page, size, Sort.by("repairId")));
        } catch (Exception e) {
            LOGGER.error("Failed while getting all repairs from database", e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert list items from RepairEntity to RepairDetailsDto
        LOGGER.debug("Convert list items from RepairEntity to RepairDetailsDto");
        List<RepairDetailsDto> repairListResponse = new ArrayList<>();
        for (RepairEntity repairEntity : repairList.getContent()) {
            repairListResponse.add(RepairConverter.fromRepairEntityToRepairDetailsDto(repairEntity));
        }

        // Build custom paginated object
        Paginated<RepairDetailsDto> results = new Paginated<>(
                repairListResponse,
                page,
                repairListResponse.size(),
                repairList.getTotalPages(),
                repairList.getTotalElements());

        // Return list of RepairDetailsDto
        return results;
    }

    /**
     * @see RepairService#updateRepairDetails(long, CreateOrUpdateRepairDto)
     */
    @Override
    public RepairDetailsDto updateRepairDetails(long repairId, CreateOrUpdateRepairDto updateRepairDto) throws RepairNotFoundException {

        // Get repair from database
        LOGGER.debug("Getting repair with id {} from database", repairId);
        RepairEntity repairEntity = repairRepository.findById(repairId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get repair with {} from database", repairId);
                    throw new RepairNotFoundException(ErrorMessages.REPAIR_NOT_FOUND);
                });

        // Update data with updateRepairDto received
        repairEntity.setRepairName(updateRepairDto.getRepairName());
        repairEntity.setRepairDescription(updateRepairDto.getRepairDescription());

        // Save changes
        LOGGER.info("Saving updates from repair with id {}", repairId);
        repairRepository.save(repairEntity);

        // Convert to RepairDetailsDto and return updated repair
        LOGGER.debug("Retrieving updated repair");
        return RepairConverter.fromRepairEntityToRepairDetailsDto(repairEntity);
    }

    /**
     * @see RepairService#deleteRepair(long)
     */
    @Override
    public void deleteRepair(long repairId) throws RepairNotFoundException {

        // Get repair from database
        LOGGER.debug("Getting repair with id {} from database", repairId);
        RepairEntity repairEntity = repairRepository.findById(repairId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get repair with {} from database", repairId);
                    throw new RepairNotFoundException(ErrorMessages.REPAIR_NOT_FOUND);
                });

        // Delete repair from database
        LOGGER.debug("Deleting repair with id {}", repairId);
        repairRepository.delete(repairEntity);
    }
}
