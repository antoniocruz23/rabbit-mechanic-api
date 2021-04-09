package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * The User Repository
 */
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

}
