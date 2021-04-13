package com.rabbit.mechanic.converter;

import com.rabbit.mechanic.command.car.CarDetailsDto;
import com.rabbit.mechanic.command.car.CreateOrUpdateCarDto;
import com.rabbit.mechanic.persistence.entity.CarEntity;

/**
 * Car Converter
 */
public class CarConverter {

    /**
     * From {@link CreateOrUpdateCarDto} to {@link CarEntity}
     * @param createOrUpdateCarDto {@link CreateOrUpdateCarDto}
     * @return {@link CarEntity}
     */
    public static CarEntity fromCreateOrUpdateCarDtoToCarEntity(CreateOrUpdateCarDto createOrUpdateCarDto) {
        return CarEntity.builder()
                .brand(createOrUpdateCarDto.getBrand())
                .engineType(createOrUpdateCarDto.getEngineType())
                .plate(createOrUpdateCarDto.getPlate())
                .build();
    }

    /**
     * From {@link CarEntity} to {@link CarDetailsDto}
     * @param carEntity {@link CarEntity}
     * @return {@link CarDetailsDto}
     */
    public static CarDetailsDto fromCarEntityToCarDetailsDto(CarEntity carEntity) {
        return CarDetailsDto.builder()
                .carId(carEntity.getCarId())
                .userId(carEntity.getUserEntity().getUserId())
                .brand(carEntity.getBrand())
                .engineType(carEntity.getEngineType())
                .plate(carEntity.getPlate())
                .build();
    }
}
