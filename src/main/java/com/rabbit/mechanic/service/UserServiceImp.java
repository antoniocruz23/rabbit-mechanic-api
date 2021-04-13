package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.converter.UserConverter;
import com.rabbit.mechanic.enumerators.UserRole;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.DataBaseCommunicationException;
import com.rabbit.mechanic.exception.UserAlreadyExistsException;
import com.rabbit.mechanic.exception.UserNotFoundException;
import com.rabbit.mechanic.command.user.CreateUserDto;
import com.rabbit.mechanic.command.user.UpdateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;
import com.rabbit.mechanic.persistence.entity.UserEntity;
import com.rabbit.mechanic.persistence.repository.UserRepository;
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
 * An {@link UserService} implementation
 */
@Service
public class UserServiceImp implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @see UserService#createUser(CreateUserDto, UserRole)
     */
    @Override
    public UserDetailsDto createUser(CreateUserDto userRegistrationDto, UserRole userRole) throws UserAlreadyExistsException {

        // Build UserEntity
        LOGGER.debug("Creating user - {} with role {}", userRegistrationDto, userRole);
        UserEntity userEntity = UserConverter.fromCreateUserDtoToUserEntity(userRegistrationDto);
        userEntity.setRole(userRole);

        // Persist user into database
        LOGGER.info("Persisting user into database");
        try {
            LOGGER.info("Saving user on database");
            userRepository.save(userEntity);

        } catch (DataIntegrityViolationException sqlException) {
            LOGGER.error("Duplicated email - {}", userEntity , sqlException);
            throw new UserAlreadyExistsException(ErrorMessages.USER_ALREADY_EXISTS);

        } catch (Exception e) {
            LOGGER.error("Failed while saving user into database {}", userEntity, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Build UserDetailsDto to return to the client
        LOGGER.debug("Retrieving created user");
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }

    /**
     * @see UserService#getUserById(long)
     */
    @Override
    public UserDetailsDto getUserById(long userId) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Convert UserDetailsDto to return to the client
        LOGGER.debug("Retrieving got user");
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }

    /**
     * @see UserService#getAllUsers(int, int)
     */
    @Override
    public Paginated<UserDetailsDto> getAllUsers(int page, int size) {

        // Get all users from database
        LOGGER.debug("Getting all users from database");
        Page<UserEntity> usersList = null;

        try {
            usersList = userRepository.findAll(PageRequest.of(page, size, Sort.by("firstName")));
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.FAILED_GETTING_ALL_USERS, e);
            throw new DataBaseCommunicationException(ErrorMessages.DATABASE_COMMUNICATION_ERROR, e);
        }

        // Convert list items from UserEntity to UserDetailsDto
        LOGGER.debug("Convert list items from UserEntity to UserDetailsDto");
        List<UserDetailsDto> usersListResponse = new ArrayList<>();
        for (UserEntity userEntity : usersList.getContent()) {
            usersListResponse.add(UserConverter.fromUserEntityToUserDetailsDto(userEntity));
        }

        //Build custom paginated object
        Paginated<UserDetailsDto> results = new Paginated<>(
                usersListResponse,
                page,
                usersListResponse.size(),
                usersList.getTotalPages(),
                usersList.getTotalElements());

        return results;
    }

    /**
     * @see UserService#updateUser(long, UpdateUserDto)
     */
    @Override
    public UserDetailsDto updateUser(long userId, UpdateUserDto updateUserDto) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Update data with userDetails received
        userEntity.setFirstName(updateUserDto.getFirstName());
        userEntity.setLastName(updateUserDto.getLastName());
        userEntity.setAddress(updateUserDto.getAddress());
        userEntity.setEmail(updateUserDto.getEmail());
        userEntity.setCellNumber(updateUserDto.getCellNumber());

        // Save changes
        LOGGER.info("Saving updates from user with id {}", userId);
        userRepository.save(userEntity);

        // Convert to UserDetailsDto and return updated user
        LOGGER.debug("Retrieving updated user");
        return UserConverter.fromUserEntityToUserDetailsDto(userEntity);
    }

    /**
     * @see UserService#deleteUser(long)
     */
    @Override
    public void deleteUser(long userId) {

        // Get user from database
        LOGGER.debug("Getting user with id {} from database", userId);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    LOGGER.error("Failed to get user with {} from database", userId);
                    throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
                });

        // Delete user from database
        LOGGER.debug("Deleting user with id {}", userId);
        userRepository.delete(userEntity);
    }
}
