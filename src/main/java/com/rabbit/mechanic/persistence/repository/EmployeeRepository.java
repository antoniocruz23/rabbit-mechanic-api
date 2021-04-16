package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.EmployeeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The employee Repository
 */
@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<EmployeeEntity, Long> {

    /**
     * Get user by username
     * @param username
     * @return
     */
    Optional<EmployeeEntity> findByUsername(String username);
}
