package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.employee.CreateEmployeeDto;
import com.rabbit.mechanic.command.employee.EmployeeDetailsDto;
import com.rabbit.mechanic.command.employee.UpdateEmployeeDto;
import com.rabbit.mechanic.converter.EmployeeConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.*;
import com.rabbit.mechanic.exception.customer.CustomerNotFoundException;
import com.rabbit.mechanic.exception.employee.EmployeeAlreadyExistsException;
import com.rabbit.mechanic.exception.employee.EmployeeNotFoundException;
import com.rabbit.mechanic.persistence.entity.EmployeeEntity;
import com.rabbit.mechanic.persistence.repository.EmployeeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link EmployeeService} implementation
 */
@Service
public class EmployeeServiceImp implements EmployeeService {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;


    public EmployeeServiceImp(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see EmployeeService#createEmployee(CreateEmployeeDto)
     */
    @Override
    public EmployeeDetailsDto createEmployee(CreateEmployeeDto createEmployeeDto) throws EmployeeAlreadyExistsException {

        // Build EmployeeEntity
        LOGGER.debug("Creating employee - {}", createEmployeeDto);
        EmployeeEntity employeeEntity = EmployeeConverter.fromCreateEmployeeDtoToEmployeeEntity(createEmployeeDto);

        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(createEmployeeDto.getPassword());
        // Set encrypted password
        employeeEntity.setEncryptedPassword(encryptedPassword);

        // Persist employee into database
        LOGGER.info("Persisting employee into database");
        try {
            LOGGER.info("Saving employee on database");
            employeeRepository.save(employeeEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated username - {}", employeeEntity, sqlException);
            throw new EmployeeAlreadyExistsException(ErrorMessages.EMPLOYEE_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving employee into database {}", employeeEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Build EmployeeDetailsDto to return to the client
        LOGGER.debug("Retrieving created employee");
        return EmployeeConverter.fromEmployeeEntityToEmployeeDetailsDto(employeeEntity);
    }

    /**
     * @see EmployeeService#getEmployeeById(long)
     */
    @Override
    public EmployeeDetailsDto getEmployeeById(long employeeId) throws EmployeeNotFoundException {

        // Get employee from database
        LOGGER.debug("Getting employee with id {} from database", employeeId);
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get employee with {} from database", employeeId);
                    throw new EmployeeNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND);
                });

        // Convert EmployeeDetailsDto to return to the client
        LOGGER.debug("Retrieving got employee");
        return EmployeeConverter.fromEmployeeEntityToEmployeeDetailsDto(employeeEntity);
    }

    /**
     * @see EmployeeService#getEmployeesList(int, int)
     */
    @Override
    public Paginated<EmployeeDetailsDto> getEmployeesList(int page, int size) {

        // Get all employees from database
        LOGGER.debug("Getting all employees from database");
        Page<EmployeeEntity> employeesList = null;

        try {
            employeesList = employeeRepository.findAll(PageRequest.of(page, size, Sort.by("firstName")));
        } catch (Exception e) {
            LOGGER.error("Failed while getting all employees from database", e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert list items from EmployeeEntity to EmployeeDetailsDto
        LOGGER.debug("Convert list items from EmployeeEntity to EmployeeDetailsDto");
        List<EmployeeDetailsDto> employeesListResponse = new ArrayList<>();
        for (EmployeeEntity employeeEntity : employeesList.getContent()) {
            employeesListResponse.add(EmployeeConverter.fromEmployeeEntityToEmployeeDetailsDto(employeeEntity));
        }

        //Build custom paginated object
        Paginated<EmployeeDetailsDto> results = new Paginated<>(
                employeesListResponse,
                page,
                employeesListResponse.size(),
                employeesList.getTotalPages(),
                employeesList.getTotalElements());

        return results;
    }

    /**
     * @see EmployeeService#updateEmployee(long, UpdateEmployeeDto)
     */
    @Override
    public EmployeeDetailsDto updateEmployee(long employeeId, UpdateEmployeeDto updateEmployeeDto) throws EmployeeNotFoundException {

        // Get user from database
        LOGGER.debug("Getting employee with id {} from database", employeeId);
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get employee with {} from database", employeeId);
                    throw new EmployeeNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND);
                });

        // Update data with employeeDetails received
        employeeEntity.setFirstName(updateEmployeeDto.getFirstName());
        employeeEntity.setLastName(updateEmployeeDto.getLastName());
        employeeEntity.setRole(updateEmployeeDto.getRole());

        // Save changes
        LOGGER.info("Saving updates from employee with id {}", employeeId);
        employeeRepository.save(employeeEntity);

        // Convert to EmployeeDetailsDto and return updated employee
        LOGGER.debug("Retrieving updated employee");
        return EmployeeConverter.fromEmployeeEntityToEmployeeDetailsDto(employeeEntity);
    }

    /**
     * @see EmployeeService#deleteEmployee(long)
     */
    @Override
    public void deleteEmployee(long employeeId) throws EmployeeNotFoundException {

        // Get employee from database
        LOGGER.debug("Getting employee with id {} from database", employeeId);
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get employee with {} from database", employeeId);
                    throw new CustomerNotFoundException(ErrorMessages.EMPLOYEE_NOT_FOUND);
                });

        // Delete employee from database
        LOGGER.debug("Deleting employee with id {}", employeeId);
        employeeRepository.delete(employeeEntity);
    }
}
