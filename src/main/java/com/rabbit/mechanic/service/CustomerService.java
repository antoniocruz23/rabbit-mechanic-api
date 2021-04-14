package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.user.CreateOrUpdateCustomerDto;
import com.rabbit.mechanic.exception.UserAlreadyExistsException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.command.user.CustomerDetailsDto;

/**
 * Common interface for customer services, provides methods to manage customers
 */
public interface CustomerService {

    /**
     * Create new customer
     * @param createUserDto {@link CreateOrUpdateCustomerDto}
     * @return {@link CustomerDetailsDto} the user created
     * @throws UserAlreadyExistsException when the user already exists
     */
    CustomerDetailsDto createCustomer(CreateOrUpdateCustomerDto createUserDto) throws UserAlreadyExistsException;

    /**
     * Get user by id
     * @param userId user id we want to get
     * @return {@link CustomerDetailsDto} the user obtained
     * @throws UserNotFoundException when the user isn't found
     */
    CustomerDetailsDto getCustomerById(long userId) throws UserNotFoundException;

    /**
     * Get all users
     * @return {@link CustomerDetailsDto} the users obtained
     */
    Paginated<CustomerDetailsDto> getCustomerList(int page, int size);

    /**
     * Update User
     * @param userId user id we want to update
     * @param updateUserDto {@link CreateOrUpdateCustomerDto}
     * @return {@link CustomerDetailsDto} the user updated
     * @throws UserNotFoundException when the user isn't found
     */
    CustomerDetailsDto updateCustomer(long userId, CreateOrUpdateCustomerDto updateUserDto) throws UserNotFoundException;

    /**
     * Delete user
     * @param userId user id we want to delete
     * @throws UserNotFoundException when the user isn't found
     */
    void deleteCustomer(long userId) throws UserNotFoundException;
}
