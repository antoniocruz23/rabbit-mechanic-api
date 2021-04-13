package com.rabbit.mechanic.command.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * CreateUserDto used to store user info when updated
 */
@Data
@Builder
public class UpdateUserDto {

    @NotBlank(message = "Must have first name")
    private String firstName;

    @NotBlank(message = "Must have last name")
    private String lastName;

    @NotBlank(message = "Must have address")
    private String address;

    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Must have cell number")
    private String cellNumber;
}
