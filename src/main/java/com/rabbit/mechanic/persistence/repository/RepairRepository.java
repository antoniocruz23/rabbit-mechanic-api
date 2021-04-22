package com.rabbit.mechanic.persistence.repository;

import com.rabbit.mechanic.persistence.entity.RepairEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * The Repair Repository
 */
public interface RepairRepository extends PagingAndSortingRepository<RepairEntity, Long> {

    /**
     * Find all repairs or just active
     * @param onlyActives
     * @param pageable
     * @return repairs paginated
     */
    @Query(value = "SELECT * \n" +
            "FROM repairs t1 \n" +
            "WHERE :onlyActives IS FALSE OR t1.end_date IS NULL",
            countQuery = "SELECT COUNT(*) \n" +
                    "FROM repairs t1 \n" +
                    "WHERE :onlyActives IS FALSE OR t1.end_date IS NULL",
            nativeQuery = true)
    Page<RepairEntity> findAllByStatus(@Param("onlyActives") boolean onlyActives, Pageable pageable);
}
