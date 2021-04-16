package com.rabbit.mechanic.security;

import com.rabbit.mechanic.command.auth.PrincipalDto;
import com.rabbit.mechanic.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Authentication provider
 */
@Component
public class EmployeeAuthenticationProvider {

    private final AuthService authService;

    public EmployeeAuthenticationProvider(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Validate jwt token
     * @param token
     * @return {@Authentication} with authenticated employee
     */
    public Authentication validateToken(String token) {

        // validate kwt token and get principal
        PrincipalDto principal = authService.validateToken(token);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                Collections.singletonList(new SimpleGrantedAuthority(principal.getEmployeeRole().name()))
        );
    }
}