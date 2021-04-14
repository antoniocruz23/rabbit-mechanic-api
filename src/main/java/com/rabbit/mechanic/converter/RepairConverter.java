package com.rabbit.mechanic.converter;

import com.rabbit.mechanic.command.repair.CreateOrUpdateRepairDto;
import com.rabbit.mechanic.command.repair.RepairDetailsDto;
import com.rabbit.mechanic.persistence.entity.RepairEntity;

/**
 * Repair converter
 */
public class RepairConverter {

    /**
     * From {@link CreateOrUpdateRepairDto} to {@link RepairEntity}
     * @param createOrUpdateRepairDto {@link CreateOrUpdateRepairDto}
     * @return {@link RepairEntity}
     */
    public static RepairEntity fromCreateOrUpdateRepairDtoToRepairEntity(CreateOrUpdateRepairDto createOrUpdateRepairDto) {
        return RepairEntity.builder()
                .repairName(createOrUpdateRepairDto.getRepairName())
                .repairDescription(createOrUpdateRepairDto.getRepairDescription())
                .startDate(createOrUpdateRepairDto.getStartDate())
                .endDate(createOrUpdateRepairDto.getEndDate())
                .price(createOrUpdateRepairDto.getPrice())
                .build();
    }

    /**
     * From {@link RepairEntity} to {@link RepairDetailsDto}
     * @param repairEntity {@link RepairEntity}
     * @return {@link RepairDetailsDto}
     */
    public static RepairDetailsDto fromRepairEntityToRepairDetailsDto(RepairEntity repairEntity) {
        return RepairDetailsDto.builder()
                .repairId(repairEntity.getRepairId())
                .carId(repairEntity.getCarEntity().getCarId())
                .repairName(repairEntity.getRepairName())
                .repairDescription(repairEntity.getRepairDescription())
                .startDate(repairEntity.getStartDate())
                .endDate(repairEntity.getEndDate())
                .price(repairEntity.getPrice())
                .build();
    }
}
