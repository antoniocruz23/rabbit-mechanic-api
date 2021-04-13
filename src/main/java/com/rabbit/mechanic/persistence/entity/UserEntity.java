package com.rabbit.mechanic.persistence.entity;

import com.rabbit.mechanic.enumerators.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The user entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 9)
    private String cellNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carId", nullable = false)
    private CarEntity carEntity;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private UserRole role;
}