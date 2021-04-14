package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.user.CreateOrUpdateCustomerDto;
import com.rabbit.mechanic.converter.CustomerConverter;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.DataBaseCommunicationException;
import com.rabbit.mechanic.exception.UserAlreadyExistsException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.command.user.CustomerDetailsDto;
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

    private static final Logger LOGGER = LogManager.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerServiceImp(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @see CustomerService#createCustomer(CreateOrUpdateCustomerDto)
     */
    @Override
    public CustomerDetailsDto createCustomer(CreateOrUpdateCustomerDto createUserDto) throws UserAlreadyExistsException {

        // Build CustomerEntity
        LOGGER.debug("Creating user - {}", createUserDto);
        CustomerEntity customerEntity = CustomerConverter.fromCreateOrUpdateCustomerDtoToCustomerEntity(createUserDto);

        // Persist user into database
        LOGGER.info("Persisting user into database");
        try {
            LOGGER.info("Saving user on database");
            customerRepository.save(customerEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated email - {}", customerEntity, sqlException);
            throw new UserAlreadyExistsException(ErrorMessages.CUSTOMER_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving user into database {}", customerEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Build CustomerDetailsDto to return to the client
        LOGGER.debug("Retrieving created user");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#getCustomerById(long)
     */
    @Override
    public CustomerDetailsDto getCustomerById(long userId) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Convert CustomerDetailsDto to return to the client
        LOGGER.debug("Retrieving got user");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#getCustomerList(int, int)
     */
    @Override
    public Paginated<CustomerDetailsDto> getCustomerList(int page, int size) {

        // Get all users from database
        LOGGER.debug("Getting all users from database");
        Page<CustomerEntity> usersList = null;

        try {
            usersList = customerRepository.findAll(PageRequest.of(page, size, Sort.by("firstName")));
        } catch (Exception e) {
            LOGGER.error("Failed while getting all users from database", e);
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
    public CustomerDetailsDto updateCustomer(long userId, CreateOrUpdateCustomerDto updateUserDto) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Update data with userDetails received
        customerEntity.setFirstName(updateUserDto.getFirstName());
        customerEntity.setLastName(updateUserDto.getLastName());
        customerEntity.setAddress(updateUserDto.getAddress());
        customerEntity.setEmail(updateUserDto.getEmail());
        customerEntity.setCellNumber(updateUserDto.getCellNumber());

        // Save changes
        LOGGER.info("Saving updates from user with id {}", userId);
        customerRepository.save(customerEntity);

        // Convert to CustomerDetailsDto and return updated user
        LOGGER.debug("Retrieving updated user");
        return CustomerConverter.fromCustomerEntityToCustomerDetailsDto(customerEntity);
    }

    /**
     * @see CustomerService#deleteCustomer(long)
     */
    @Override
    public void deleteCustomer(long userId) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        CustomerEntity customerEntity = customerRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
                });

        // Delete user from database
        LOGGER.debug("Deleting user with id {}", userId);
        customerRepository.delete(customerEntity);
    }
}
