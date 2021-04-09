package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.enumerators.UserRole;
import com.rabbit.mechanic.command.user.CreateUserDto;
import com.rabbit.mechanic.command.user.UpdateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;
import com.rabbit.mechanic.service.UserServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.awt.print.Pageable;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

/**
 * User Controller who provides endpoints
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);
    private final UserServiceImp userService;

    public UserController(UserServiceImp userService) {
        this.userService = userService;
    }

    /**
     * Create new User
     * @param createUserDto {@link CreateUserDto}
     * @return {@link UserDetailsDto} user created and Created httpStatus
     */
    @PostMapping
    public ResponseEntity<UserDetailsDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {

        LOGGER.info("Request to create - {} and role {}.", createUserDto, UserRole.CUSTOMER);
        UserDetailsDto userDetailsDto =  userService.createUser(createUserDto, UserRole.CUSTOMER);

        LOGGER.info("Service retrieved created user {}", userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, HttpStatus.CREATED);
    }

    /**
     * Get user by id
     * @param userId user id we want to get
     * @return {@link UserDetailsDto} the user wanted and Ok httpStatus
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> getUserById(@PathVariable long userId) {

        LOGGER.info("Request to get user with id {}", userId);
        UserDetailsDto userDetailsDto = userService.getUserById(userId);

        LOGGER.info("Service retrieved to got user {}", userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, OK);
    }

    /**
     * Get all users
     * @return {@link UserDetailsDto} list of all users and Ok httpStatus
     */
    @GetMapping
    public ResponseEntity<Paginated<UserDetailsDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "5") int size) {

        LOGGER.info("Request to get all users");
        Paginated<UserDetailsDto> usersList = userService.getAllUsers(page, size);

        LOGGER.info("Service retrieved to got users {}", usersList);
        return new ResponseEntity<>(usersList, OK);
    }

    /**
     * Update User
     * @param userId user id we want to update
     * @param updateUserDto {@link UpdateUserDto}
     * @return {@link UserDetailsDto} user updated and Ok httpStatus
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserDetailsDto> updateUser(@PathVariable long userId,
                                                     @Valid @RequestBody UpdateUserDto updateUserDto) {

        LOGGER.info("Request to update user with id {}", userId);
        UserDetailsDto userDetailsDto = userService.updateUser(userId, updateUserDto);

        LOGGER.info("Service retrieved to update user {}", userDetailsDto);
        return new ResponseEntity<>(userDetailsDto, OK);
    }

    /**
     * Delete user
     * @param userId user id we want to delete
     * @return Ok httpStatus
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable long userId) {

        LOGGER.info("Request to delete user with id {}", userId);
        userService.deleteUser(userId);

        LOGGER.info("User with id {} deleted successfully", userId);
        return new ResponseEntity(OK);
    }
}
