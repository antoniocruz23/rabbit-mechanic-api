package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.EmployeeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The employee Repository
 */
@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<EmployeeEntity, Long> {
}
