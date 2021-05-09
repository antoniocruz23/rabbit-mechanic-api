package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.employee.CreateEmployeeDto;
import com.rabbit.mechanic.command.employee.EmployeeDetailsDto;
import com.rabbit.mechanic.command.employee.UpdateEmployeeDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.EmployeeServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * User Employee who provides endpoints
 */
@RestController
@RequestMapping("/api/employees")
@PreAuthorize("@authorized.hasRole(\"ADMIN\")")
public class EmployeeController {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(EmployeeController.class);
    private final EmployeeServiceImp employeeServiceImp;

    public EmployeeController(EmployeeServiceImp employeeServiceImp) {
        this.employeeServiceImp = employeeServiceImp;
    }

    /**
     * Create new Employee
     * @param createEmployeeDto {@link CreateEmployeeDto}
     * @return {@link EmployeeDetailsDto} employee created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<EmployeeDetailsDto> createEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto) {

        LOGGER.info("Request to create - {}.", createEmployeeDto);
        EmployeeDetailsDto employeeDetailsDto;
        try {
            employeeDetailsDto =  employeeServiceImp.createEmployee(createEmployeeDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to created employee - {}", createEmployeeDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Employee created successfully. Retrieving created employee with id {}", employeeDetailsDto.getEmployeeId());
        return new ResponseEntity<>(employeeDetailsDto, HttpStatus.CREATED);
    }

    /**
     * Get employee by id
     * @param employeeId employee id we want to get
     * @return {@link EmployeeDetailsDto} the employee wanted and Ok httpStatus
     */
    @GetMapping("/{employeeId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\") || " +
                "@authorized.isEmployee(#employeeId)")
    public ResponseEntity<EmployeeDetailsDto> getEmployeeById(@PathVariable long employeeId) {

        LOGGER.info("Request to get employee with id {}", employeeId);
        EmployeeDetailsDto employeeDetailsDto;
        try {
            employeeDetailsDto = employeeServiceImp.getEmployeeById(employeeId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get employee with id {}", employeeId, e);
                throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieved employee with id {}", employeeId);
        return new ResponseEntity<>(employeeDetailsDto, OK);
    }

    /**
     * Get employee list
     * @return {@link EmployeeDetailsDto} list of all employees and Ok httpStatus
     */
    @GetMapping
    public ResponseEntity<Paginated<EmployeeDetailsDto>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "20") int size) {

        LOGGER.info("Request to get all employees");
        Paginated<EmployeeDetailsDto> employeeList;
        try {
            employeeList = employeeServiceImp.getEmployeesList(page, size);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get employees list", e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving employees list");
        return new ResponseEntity<>(employeeList, OK);
    }

    /**
     * Update Employee
     * @param employeeId employee id we want to update
     * @param updateEmployeeDto {@link UpdateEmployeeDto}
     * @return {@link EmployeeDetailsDto} employee updated and Ok httpStatus
     */
    @PutMapping("/{employeeId}")
    @PreAuthorize("@authorized.hasRole(\"ADMIN\")")
    public ResponseEntity<EmployeeDetailsDto> updateEmployee(@PathVariable long employeeId,
                                                 @Valid @RequestBody UpdateEmployeeDto updateEmployeeDto) {

        LOGGER.info("Request to update employee with id {}", employeeId);
        EmployeeDetailsDto employeeDetailsDto;
        try {
            employeeDetailsDto = employeeServiceImp.updateEmployee(employeeId, updateEmployeeDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to update employee with id {} - {}", employeeId, updateEmployeeDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Employee with id {} updated successfully. Retrieving updated data", employeeId);
        return new ResponseEntity<>(employeeDetailsDto, OK);
    }

    /**
     * Delete employee
     * @param employeeId employee id we want to delete
     * @return Ok httpStatus
     */
    @DeleteMapping("/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable long employeeId) {

        LOGGER.info("Request to delete employee with id {}", employeeId);
        try {
            employeeServiceImp.deleteEmployee(employeeId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to delete employee with id {}", employeeId, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Employee with id {} deleted successfully", employeeId);
        return new ResponseEntity(OK);
    }
}
