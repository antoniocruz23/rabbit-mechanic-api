package com.rabbit.mechanic.command.car;

import com.rabbit.mechanic.enumerators.CarBrands;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * CreateOrUpdateCarDto used to store car info when creating or updating cars
 */
@Data
@Builder
public class CreateOrUpdateCarDto {

    @NotNull(message = "Must have a user id")
    private long userId;

    @NotNull(message = "Must have a car brand")
    private CarBrands brand;

    @NotNull(message = "Must have a engine type")
    private String engineType;

    @NotBlank(message = "Must have plate")
    private String plate;
}
