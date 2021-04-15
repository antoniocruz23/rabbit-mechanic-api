package com.rabbit.mechanic.command.employee;

import com.rabbit.mechanic.enumerators.EmployeeRoles;
import lombok.Builder;
import lombok.Data;

/**
 * EmployeeDetailsDto used to respond with employee details
 */
@Data
@Builder
public class EmployeeDetailsDto {

    private long employeeId;
    private String firstName;
    private String lastName;
    private String username;
    private EmployeeRoles role;
}
