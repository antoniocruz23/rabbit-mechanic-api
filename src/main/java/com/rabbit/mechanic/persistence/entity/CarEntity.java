package com.rabbit.mechanic.persistence.entity;

import com.rabbit.mechanic.enumerators.CarBrands;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * The car entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customerEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarBrands brand;

    @Column(nullable = false)
    private String engineType;

    @Column(nullable = false, length = 10, unique = true)
    private String plate;

    @OneToMany(mappedBy = "carEntity")
    private List<RepairEntity> repairs;
}
