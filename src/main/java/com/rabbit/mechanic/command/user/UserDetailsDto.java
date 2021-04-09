package com.rabbit.mechanic.command.user;

import lombok.Builder;
import lombok.Data;

/**
 * UserDetailsDto used to respond with user details
 */
@Data
@Builder
public class UserDetailsDto {

    private long userId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String cellNumber;
}
