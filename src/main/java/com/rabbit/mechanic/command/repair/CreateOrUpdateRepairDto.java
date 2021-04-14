package com.rabbit.mechanic.command.repair;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * CreateOrUpdateRepairDto used to store repair info when creating or updating repair
 */
@Data
@Builder
public class CreateOrUpdateRepairDto {

    @NotNull(message = "Must have a repair id")
    private long repairId;

    @NotNull(message = "Must have a car id")
    private long carId;

    @NotNull(message = "Must have a repair name")
    private String repairName;

    @NotNull(message = "Must have a description name")
    private String repairDescription;

    @NotNull(message = "Must have a start date")
    private Date startDate;

    private Date endDate;
    private BigDecimal price;
}
