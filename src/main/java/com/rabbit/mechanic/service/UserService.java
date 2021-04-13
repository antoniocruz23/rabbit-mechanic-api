package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.enumerators.UserRole;
import com.rabbit.mechanic.exception.UserAlreadyExistsException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.command.user.CreateOrUpdateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;

/**
 * Common interface for user services, provides methods to manage users
 */
public interface UserService {

    /**
     * Create new user
     * @param createUserDto {@link CreateOrUpdateUserDto}
     * @param userRole {@link UserRole}
     * @return {@link UserDetailsDto} the user created
     * @throws UserAlreadyExistsException when the user already exists
     */
    UserDetailsDto createUser(CreateOrUpdateUserDto createUserDto, UserRole userRole) throws UserAlreadyExistsException;

    /**
     * Get user by id
     * @param userId user id we want to get
     * @return {@link UserDetailsDto} the user obtained
     * @throws UserNotFoundException when the user aren't found
     */
    UserDetailsDto getUserById(long userId) throws UserNotFoundException;

    /**
     * Get all users
     * @return {@link UserDetailsDto} the users obtained
     */
    Paginated<UserDetailsDto> getAllUsers(int page, int size);

    /**
     * Update User
     * @param userId user id we want to update
     * @param updateUserDto {@link CreateOrUpdateUserDto}
     * @return {@link UserDetailsDto} the user updated
     * @throws UserNotFoundException when the user aren't found
     */
    UserDetailsDto updateUser(long userId, CreateOrUpdateUserDto updateUserDto) throws UserNotFoundException;

    /**
     * Delete user
     * @param userId user id we want to delete
     * @throws UserNotFoundException when the user aren't found
     */
    void deleteUser(long userId) throws UserNotFoundException;
}
