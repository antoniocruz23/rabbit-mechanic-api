package com.rabbit.mechanic.command.car;

import com.rabbit.mechanic.enumerators.CarBrands;
import lombok.Builder;
import lombok.Data;

/**
 * CarDetailsDto used to respond with car details
 */
@Data
@Builder
public class CarDetailsDto {

    private long carId;
    private long userId;
    private CarBrands brand;
    private String engineType;
    private String plate;
}
