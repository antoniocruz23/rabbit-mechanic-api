package com.rabbit.mechanic.command.employee;

import com.rabbit.mechanic.enumerators.EmployeeRoles;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * CreateEmployeeDto used to store worker info when created
 */
@Data
@Builder
public class CreateEmployeeDto {

    @NotBlank(message = "Must have first name")
    private String firstName;

    @NotBlank(message = "Must have last name")
    private String lastName;

    @NotBlank(message = "Must have username")
    private String username;

    @NotBlank(message = "Must have password")
    private String password;

    @NotNull(message = "Must have role")
    private EmployeeRoles role;

    @Override
    public String toString() {
        return "CreateUserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='***'" +
                '}';
    }
}
