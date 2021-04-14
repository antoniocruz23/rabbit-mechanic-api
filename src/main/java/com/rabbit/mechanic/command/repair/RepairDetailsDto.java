package com.rabbit.mechanic.command.repair;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * RepairDetailsDto used to respond with repair details
 */
@Data
@Builder
public class RepairDetailsDto {

    private long repairId;
    private long carId;
    private String repairName;
    private String repairDescription;
    private Date startDate;
    private Date endDate;
    private BigDecimal price;
}
