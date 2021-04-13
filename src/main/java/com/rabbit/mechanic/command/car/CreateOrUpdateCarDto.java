package com.rabbit.mechanic.command.car;

import com.rabbit.mechanic.enumerators.CarBrands;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * CreateOrUpdateCarDto used to store car info when creating or updating cars
 */
@Data
@Builder
public class CreateOrUpdateCarDto {

    private CarBrands brand;
    private String engineType;

    @NotBlank(message = "Must have plate")
    private String plate;
}
