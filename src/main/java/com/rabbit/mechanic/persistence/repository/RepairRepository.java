package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.RepairEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The Repair Repository
 */
public interface RepairRepository extends PagingAndSortingRepository<RepairEntity, Long> {
}
