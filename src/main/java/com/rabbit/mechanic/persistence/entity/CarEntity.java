package com.rabbit.mechanic.persistence.entity;

import com.rabbit.mechanic.enumerators.CarBrands;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private CarBrands brand;

    @Column(nullable = false)
    private String engineType;

    @Column(nullable = false, length = 8, unique = true)
    private String plate;
}
