package com.rabbit.mechanic.command.auth;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CredentialsDto {

    @NotBlank(message = "Username must be provided")
    private String username;

    @NotBlank(message = "Password must be provided")
    private String password;

    /**
     * Override to String to avoid show the password
     * in the logs if printing the entire object
     * @return
     */
    @Override
    public String toString() {
        return "CredentialsDto{" +
                "username='" + username + '\'' +
                ", password='***'" +
                '}';
    }
}