package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.auth.CredentialsDto;
import com.rabbit.mechanic.command.auth.LoggedInDto;
import com.rabbit.mechanic.command.auth.PrincipalDto;

/**
 * Common interface for authorization operations
 */
public interface AuthService {

    /**
     * Login employee
     * @param credentialsDto
     * @return {@link LoggedInDto} logged in employee details
     */
    LoggedInDto loginEmployee(CredentialsDto credentialsDto);

    /**
     * Validate token
     * @param token
     * @return {@link PrincipalDto} principal authenticated
     */
    PrincipalDto validateToken(String token);
}
