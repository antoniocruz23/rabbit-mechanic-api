package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.customer.CreateOrUpdateCustomerDto;
import com.rabbit.mechanic.command.customer.CustomerDetailsDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.CustomerServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

/**
 * Customer Controller who provides endpoints
 */
@RestController
@RequestMapping("/api/customers")
@PreAuthorize("@authorized.hasRole(\"RECEPTIONIST\") ||" +
        "@authorized.hasRole(\"ADMIN\")")
public class CustomerController {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(CustomerController.class);
    private final CustomerServiceImp customerService;

    public CustomerController(CustomerServiceImp customerService) {
        this.customerService = customerService;
    }

    /**
     * Create new Customer
     * @param createCustomerDto {@link CreateOrUpdateCustomerDto}
     * @return {@link CustomerDetailsDto} Customer created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<CustomerDetailsDto> createCustomer(@Valid @RequestBody CreateOrUpdateCustomerDto createCustomerDto) {

        LOGGER.info("Request to create - {}.", createCustomerDto);
        CustomerDetailsDto customerDetailsDto;
        try {
            customerDetailsDto = customerService.createCustomer(createCustomerDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to created customer - {}", createCustomerDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("User created successfully. Retrieving created user with id {}", customerDetailsDto.getCustomerId());
        return new ResponseEntity<>(customerDetailsDto, HttpStatus.CREATED);
    }

    /**
     * Get Customer by id
     * @param customerId customer id we want to get
     * @return {@link CustomerDetailsDto} the customer wanted and Ok httpStatus
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDetailsDto> getCustomerById(@PathVariable long customerId) {

        LOGGER.info("Request to get customer with id {}", customerId);
        CustomerDetailsDto customerDetailsDto;
        try {
            customerDetailsDto = customerService.getCustomerById(customerId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get customer with id {}", customerId, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving customer with id {}", customerId);
        return new ResponseEntity<>(customerDetailsDto, OK);
    }

    /**
     * Get customers list
     * @return {@link CustomerDetailsDto} list of all customers and Ok httpStatus
     */
    @GetMapping
    public ResponseEntity<Paginated<CustomerDetailsDto>> getCustomerList(@RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "20") int size) {

        LOGGER.info("Request to get customers list - page: {}, size: {}", page, size);
        Paginated<CustomerDetailsDto> customersList;
        try {
            customersList = customerService.getCustomerList(page, size);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to get customers list", e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Retrieving customers list");
        return new ResponseEntity<>(customersList, OK);
    }

    /**
     * Update Customer
     * @param customerId customer id we want to update
     * @param updateCustomerDto {@link CreateOrUpdateCustomerDto}
     * @return {@link CustomerDetailsDto} customer updated and Ok httpStatus
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDetailsDto> updateCustomer(@PathVariable long customerId,
                                                             @Valid @RequestBody CreateOrUpdateCustomerDto updateCustomerDto) {

        LOGGER.info("Request to update customer with id {}", customerId);
        CustomerDetailsDto customerDetailsDto;
        try {
            customerDetailsDto = customerService.updateCustomer(customerId, updateCustomerDto);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to update customer with id {} - {}", customerId, updateCustomerDto, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Customer with id {} updated successfully. Retrieving updated customer", customerId);
        return new ResponseEntity<>(customerDetailsDto, OK);
    }

    /**
     * Delete customer
     * @param customerId customer id we want to delete
     * @return Ok httpStatus
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteUser(@PathVariable long customerId) {

        LOGGER.info("Request to delete customer with id {}", customerId);
        try {
            customerService.deleteCustomer(customerId);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to delete customer with id {}", customerId, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }
        LOGGER.info("Customer with id {} deleted successfully", customerId);
        return new ResponseEntity(OK);
    }
}
