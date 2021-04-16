package com.rabbit.mechanic.command.auth;

import com.rabbit.mechanic.enumerators.EmployeeRoles;
import lombok.Builder;
import lombok.Data;

/**
 * Principal information
 * principal definition - entity who can authenticate (employee, other service, third-parties...)
 */
@Data
@Builder
public class PrincipalDto {
    private Long employeeId;
    private String firstName;
    private String lastName;
    private EmployeeRoles employeeRole;
}
