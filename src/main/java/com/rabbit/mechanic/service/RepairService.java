package com.rabbit.mechanic.service;

import com.rabbit.mechanic.command.Paginated;
import com.rabbit.mechanic.command.repair.CreateOrUpdateRepairDto;
import com.rabbit.mechanic.command.repair.RepairDetailsDto;
import com.rabbit.mechanic.exception.repair.RepairAlreadyExistsException;
import com.rabbit.mechanic.exception.repair.RepairNotFoundException;

/**
 * Common interface for repair services, provides methods to manage repairs
 */
public interface RepairService {

    /**
     * Create new repair
     * @param createRepairDto {@link CreateOrUpdateRepairDto}
     * @return {@link RepairDetailsDto} the repair created
     * @throws RepairAlreadyExistsException when the repair already exists
     */
    RepairDetailsDto createRepair(CreateOrUpdateRepairDto createRepairDto) throws RepairAlreadyExistsException;

    /**
     * Get repair by id
     * @param repairId repair id we want to get
     * @return {@link RepairDetailsDto} the repair obtained
     * @throws RepairNotFoundException when the repair isn't found
     */
    RepairDetailsDto getRepairById(long repairId) throws RepairNotFoundException;

    /**
     * Get Repairs List
     * @return {@link RepairDetailsDto} the repairs obtained
     */
    Paginated<RepairDetailsDto> getRepairsList(int page, int size);

    /**
     * Update repair details
     * @param repairId repair id we want to update
     * @param updateRepairDto {@link CreateOrUpdateRepairDto}
     * @return {@link RepairDetailsDto} the repair updated
     * @throws RepairNotFoundException when the repair isn't found
     */
    RepairDetailsDto updateRepairDetails(long repairId, CreateOrUpdateRepairDto updateRepairDto) throws RepairNotFoundException;

    /**
     * Delete Repair
     * @param repairId repair id we want to delete
     * @throws RepairNotFoundException when the repair isn't found
     */
    void deleteRepair(long repairId) throws RepairNotFoundException;
}
