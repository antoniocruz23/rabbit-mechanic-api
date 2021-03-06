package com.rabbit.mechanic.command.employee;

import com.rabbit.mechanic.enumerators.EmployeeRoles;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * CreateEmployeeDto used to store employee info when updated
 */
@Data
@Builder
public class UpdateEmployeeDto {

    @NotBlank(message = "Must have first name")
    private String firstName;

    @NotBlank(message = "Must have last name")
    private String lastName;

    @NotNull(message = "Must have role")
    private EmployeeRoles role;
}
