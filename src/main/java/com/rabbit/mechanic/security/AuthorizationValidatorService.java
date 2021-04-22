package com.rabbit.mechanic.security;

import com.rabbit.mechanic.command.auth.PrincipalDto;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthorizationValidatorService {

    public boolean hasRole(String role) {
        return role.equals(getPrincipal().getEmployeeRole().name());
    }

    public boolean isEmployee(Long employeeId) {
        return employeeId.equals(getPrincipal().getEmployeeId());
    }

    private PrincipalDto getPrincipal() {
        return (PrincipalDto) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }
}
