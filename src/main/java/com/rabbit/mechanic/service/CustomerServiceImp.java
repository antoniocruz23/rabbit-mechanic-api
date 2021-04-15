package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.customer.CreateOrUpdateCustomerDto;
import com.rabbit.mechanic.converter.CustomerConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.CustomerNotFoundException;
import com.rabbit.mechanic.exception.DataBaseCommunicationException;
import com.rabbit.mechanic.exception.CustomerAlreadyExistsException;
import com.rabbit.mechanic.command.customer.CustomerDetailsDto;
import com.rabbit.mechanic.persistence.entity.CustomerEntity;
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
 * An {@link CustomerService} implementation
 */
@Service
public class CustomerServiceImp implements CustomerService {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerServiceImp(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @see CustomerService#createCustomer(CreateOrUpdateCustomerDto)
     */
    @Override
    public CustomerDetailsDto createCustomer(CreateOrUpdateCustomerDto createUserDto) throws CustomerAlreadyExistsException {

        // Build CustomerEntity
        LOGGER.debug("Creating customer - {}", createUserDto);
        CustomerEntity customerEntity = CustomerConverter.fromCreateOrUpdateCustomerDtoToCustomerEntity(createUserDto);

        // Persist user into database
        LOGGER.info("Persisting customer into database");
        try {
            LOGGER.info("Saving customer on database");
            customerRepository.save(customerEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated email - {}", customerEntity, sqlException);
            throw new CustomerAlreadyExistsException(ErrorMessages.CUSTOMER_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving customer into database {}", customerEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Build CustomerDetailsDto to return to the client
        LOGGER.debug("Retrieving created customer");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#getCustomerById(long)
     */
    @Override
    public CustomerDetailsDto getCustomerById(long customerId) {

        // Get user from database
        LOGGER.debug("Getting customer with id {} from database", customerId);
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get customer with {} from database", customerId);
                    throw new CustomerNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Convert CustomerDetailsDto to return to the client
        LOGGER.debug("Retrieving got customer");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#getCustomerList(int, int)
     */
    @Override
    public Paginated<CustomerDetailsDto> getCustomerList(int page, int size) {

        // Get all users from database
        LOGGER.debug("Getting all customers from database");
        Page<CustomerEntity> usersList = null;

        try {
            usersList = customerRepository.findAll(PageRequest.of(page, size, Sort.by("firstName")));
        } catch (Exception e) {
            LOGGER.error("Failed while getting all customers from database", e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert list items from CustomerEntity to CustomerDetailsDto
        LOGGER.debug("Convert list items from CustomerEntity to CustomerDetailsDto");
        List<CustomerDetailsDto> usersListResponse = new ArrayList<>();
        for (CustomerEntity customerEntity : usersList.getContent()) {
            usersListResponse.add(CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity));
        }

        //Build custom paginated object
        Paginated<CustomerDetailsDto> results = new Paginated<>(
                usersListResponse,
                page,
                usersListResponse.size(),
                usersList.getTotalPages(),
                usersList.getTotalElements());

        return results;
    }

    /**
     * @see CustomerService#updateCustomer(long, CreateOrUpdateCustomerDto)
     */
    @Override
    public CustomerDetailsDto updateCustomer(long customerId, CreateOrUpdateCustomerDto updateUserDto) {

        // Get user from database
        LOGGER.debug("Getting customer with id {} from database", customerId);
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get customer with {} from database", customerId);
                    throw new CustomerNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Update data with userDetails received
        customerEntity.setFirstName(updateUserDto.getFirstName());
        customerEntity.setLastName(updateUserDto.getLastName());
        customerEntity.setAddress(updateUserDto.getAddress());
        customerEntity.setEmail(updateUserDto.getEmail());
        customerEntity.setCellNumber(updateUserDto.getCellNumber());

        // Save changes
        LOGGER.info("Saving updates from customer with id {}", customerId);
        customerRepository.save(customerEntity);

        // Convert to CustomerDetailsDto and return updated customer
        LOGGER.debug("Retrieving updated customer");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#deleteCustomer(long)
     */
    @Override
    public void deleteCustomer(long customerId) {

        // Get customer from database
        LOGGER.debug("Getting customer with id {} from database", customerId);
        CustomerEntity customerEntity = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get customer with {} from database", customerId);
                    throw new CustomerNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Delete customer from database
        LOGGER.debug("Deleting customer with id {}", customerId);
        customerRepository.delete(customerEntity);
    }
}
