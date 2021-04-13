package com.rabbit.mechanic.command.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * CreateUserDto used to store user info when created
 */
@Data
@Builder
public class CreateUserDto {

    @NotBlank(message = "Must have first name")
    private String firstName;

    @NotBlank(message = "Must have last name")
    private String lastName;

    @NotBlank(message = "Must have address")
    private String address;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Must have a cell number")
    private String cellNumber;

    @Override
    public String toString() {
        return "CreateUserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", cellNumber='" + cellNumber + '\'' +
                '}';
    }
}
