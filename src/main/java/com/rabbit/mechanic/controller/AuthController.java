package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.auth.CredentialsDto;
import com.rabbit.mechanic.command.auth.LoggedInDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for authentication operations
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login employee with username and password
     * @param credentials
     * @return {@link LoggedInDto} with employee info and jwt token
     */
    @PostMapping("/login")
    public ResponseEntity<LoggedInDto> login(@RequestBody CredentialsDto credentials) {

        LOGGER.info("Request to login employee with username {}", credentials.getUsername());
        LoggedInDto loggedIn;
        try {
            loggedIn = authService.loginEmployee(credentials);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to logging employee - {}", credentials, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }

        LOGGER.info("Employee id {}, username {} logged in successfully. Retrieving jwt token",
                loggedIn.getPrincipal().getEmployeeId(), credentials.getUsername());

        return new ResponseEntity<>(loggedIn, HttpStatus.OK);
    }
}
