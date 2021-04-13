package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.CarEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The Car Repository
 */
public interface CarRepository extends PagingAndSortingRepository<CarEntity, Long> {
}
