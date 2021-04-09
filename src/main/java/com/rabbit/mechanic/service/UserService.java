package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.enumerators.UserRole;
import com.rabbit.mechanic.exception.UserAlreadyExistsException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.command.user.CreateUserDto;
import com.rabbit.mechanic.command.user.UpdateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;

import java.util.List;

/**
 * Common interface for user services, provides methods to manage users
 */
public interface UserService {

    /**
     * Create new user
     * @param userRegistrationDto {@link CreateUserDto}
     * @param userRole {@link UserRole}
     * @return {@link UserDetailsDto} the user created
     * @throws UserAlreadyExistsException when the user already exists
     */
    UserDetailsDto createUser(CreateUserDto userRegistrationDto, UserRole userRole) throws UserAlreadyExistsException;

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
     * @param updateUserDto {@link UpdateUserDto}
     * @return {@link UserDetailsDto} the user updated
     * @throws UserNotFoundException when the user aren't found
     */
    UserDetailsDto updateUser(long userId, UpdateUserDto updateUserDto) throws UserNotFoundException;

    /**
     * Delete user
     * @param userId user id we want to delete
     * @throws UserNotFoundException when the user aren't found
     */
    void deleteUser(long userId) throws UserNotFoundException;
}
