package com.rabbit.mechanic.controller;

import com.rabbit.mechanic.command.auth.CredentialsDto;
import com.rabbit.mechanic.command.auth.LoggedInDto;
import com.rabbit.mechanic.command.auth.PrincipalDto;
import com.rabbit.mechanic.error.ErrorMessages;
import com.rabbit.mechanic.exception.RabbitMechanicException;
import com.rabbit.mechanic.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import static com.rabbit.mechanic.security.CookieAuthFilter.COOKIE_NAME;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for authentication operations
 */
@RestController
@RequestMapping("/api/auth")
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

            ResponseCookie cookie = ResponseCookie
                    .from(COOKIE_NAME, loggedIn.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .maxAge(24 * 60 * 60)
                    .path("/")
                    .build();

            LOGGER.info("Employee logged in successfully. Retrieving jwt token and setting cookie");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(loggedIn);

        } catch (RabbitMechanicException e) {
            // Since RabbitMechanicException exceptions are thrown by us, we just throw them
            throw e;

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to logging employee - {}", credentials, e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }
    }

    /**
     * Logout employee and delete cookie
     * @param principal
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal PrincipalDto principal) {
        LOGGER.info("Request to logout employee with id {}", principal.getEmployeeId());

        try {
            SecurityContextHolder.clearContext();

            ResponseCookie deleteCookie = ResponseCookie
                    .from(COOKIE_NAME, null)
                    .httpOnly(true)
                    .secure(false)
                    .maxAge(0L)
                    .path("/")
                    .build();

            LOGGER.info("Successfully logged out. Deleting cookie.");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .build();

        } catch (Exception e) {
            // With all others exceptions we log them and throw a generic exception
            LOGGER.error("Failed to logout user - {}", principal.getEmployeeId(), e);
            throw new RabbitMechanicException(ErrorMessages.OPERATION_FAILED, e);
        }
    }
}
