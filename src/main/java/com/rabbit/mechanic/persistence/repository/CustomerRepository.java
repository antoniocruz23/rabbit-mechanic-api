package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.CustomerEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The Customer Repository
 */
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long> {

}
