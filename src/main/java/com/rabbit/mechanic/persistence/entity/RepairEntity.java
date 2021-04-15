package com.rabbit.mechanic.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * The repair entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repairs")
public class RepairEntity extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long repairId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carId", nullable = false)
    private CarEntity carEntity;

    @Column(nullable = false)
    private String repairName;

    @Column(nullable = false)
    private String repairDescription;

    @Column(nullable = false)
    private Date startDate;

    @Column()
    private Date endDate;

    @Column()
    private BigDecimal price;
}
