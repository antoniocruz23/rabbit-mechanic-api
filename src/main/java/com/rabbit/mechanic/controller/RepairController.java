package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.repair.CreateOrUpdateRepairDto;
import com.rabbit.mechanic.command.repair.RepairDetailsDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.RepairServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * Repair Controller who provides endpoints
 */
@RestController
@RequestMapping("/repairs")
public class RepairController {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(RepairController.class);
    private final RepairServiceImp repairServiceImp;

    public RepairController(RepairServiceImp repairServiceImp) {
        this.repairServiceImp = repairServiceImp;
    }

    /**
     * Create new Repair
     * @param createRepairDto {@link CreateOrUpdateRepairDto}
     * @return {@link RepairDetailsDto} repair created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<RepairDetailsDto> createRepair(@Valid @RequestBody CreateOrUpdateRepairDto createRepairDto) {

        LOGGER.info("Request to create - {}.", createRepairDto);
        RepairDetailsDto repairDetailsDto;
        try {
            repairDetailsDto = repairServiceImp.createRepair(createRepairDto);
        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;
        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to created repair - {}", createRepairDto, e);
            throw new RabbitMechanicException(ErrorMessages.REPAIR_ALREADY_EXISTS, e);
        }

        LOGGER.info("Repair created successfully. Retrieving created repair with id {}", repairDetailsDto.getRepairId());
        return new ResponseEntity<>(repairDetailsDto, HttpStatus.CREATED);
    }

    /**
     * Get repair by id
     * @param repairId repair id we want to get
     * @return {@link RepairDetailsDto} the repair wanted and Ok httpStatus
     */
    @GetMapping("/{repairId}")
    public ResponseEntity<RepairDetailsDto> getRepairById(@PathVariable long repairId) {

        LOGGER.info("Request to get repair with id {}", repairId);
        RepairDetailsDto repairDetailsDto;
        try {
            repairDetailsDto = repairServiceImp.getRepairById(repairId);
        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;
        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get repair with id {}", repairId, e);
            throw new RabbitMechanicException(ErrorMessages.REPAIR_NOT_FOUND, e);
        }

        LOGGER.info("Retrieving repair with id {}", repairId);
        return new ResponseEntity<>(repairDetailsDto, OK);
    }

    /**
     * Get Repairs List
     * @return {@link RepairDetailsDto} list of all repairs and Ok httpStatus
     */
    @GetMapping
    public ResponseEntity<Paginated<RepairDetailsDto>> getRepairsList(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "5") int size) {

        LOGGER.info("Request to get repairs list - page: {}, size: {}", page, size);
        Paginated<RepairDetailsDto> repairsList;
        try {
            repairsList = repairServiceImp.getRepairsList(page, size);
        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;
        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get repairs list", e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving repairs list");
        return new ResponseEntity<>(repairsList, HttpStatus.OK);
    }

    /**
     * Update repair
     * @param repairId repair id we want to update
     * @param updateRepairDto {@link CreateOrUpdateRepairDto}
     * @return {@link RepairDetailsDto} repair updated and Ok httpStatus
     */
    @PutMapping("/{repairId}")
    public ResponseEntity<RepairDetailsDto> updateRepair(@PathVariable long repairId,
                                                   @Valid @RequestBody CreateOrUpdateRepairDto updateRepairDto) {

        LOGGER.info("Request to update repair with id {} - {}", repairId, updateRepairDto);
        RepairDetailsDto repairDetailsDto;
        try {
            repairDetailsDto = repairServiceImp.updateRepairDetails(repairId, updateRepairDto);
        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;
        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to update repair with id {} - {}", repairId, updateRepairDto, e);
            throw new RabbitMechanicException(ErrorMessages.REPAIR_NOT_FOUND, e);
        }

        LOGGER.info("Repair with id {} updated successfully. Retrieving updated data", repairId);
        return new ResponseEntity<>(repairDetailsDto, HttpStatus.OK);
    }

    /**
     * Delete repair
     * @param repairId repair id we want to delete
     * @return Ok httpStatus
     */
    @DeleteMapping("/{repairId}")
    public ResponseEntity deleteRepair(@PathVariable long repairId) {

        LOGGER.info("Request to delete car with id {}", repairId);
        try {
            repairServiceImp.deleteRepair(repairId);
        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;
        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to delete repair with id {}", repairId, e);
            throw new RabbitMechanicException(ErrorMessages.REPAIR_NOT_FOUND, e);
        }

        LOGGER.info("Repair with id {} deleted successfully", repairId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
