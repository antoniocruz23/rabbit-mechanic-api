package com.rabbit.mechanic.command.user;

import lombok.Builder;
import lombok.Data;

/**
 * CustomerDetailsDto used to respond with Customer details
 */
@Data
@Builder
public class CustomerDetailsDto {

    private long customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String cellNumber;
}
