package com.rabbit.mechanic.converter;

import com.rabbit.mechanic.command.employee.CreateEmployeeDto;
import com.rabbit.mechanic.command.employee.EmployeeDetailsDto;
import com.rabbit.mechanic.persistence.entity.EmployeeEntity;

/**
 * Employee converter
 */
public class EmployeeConverter {

    /**
     * From {@link CreateEmployeeDto} to {@link EmployeeEntity}
     * @param createEmployeeDto {@link CreateEmployeeDto}
     * @return {@link EmployeeEntity}
     */
    public static EmployeeEntity fromCreateEmployeeDtoToEmployeeEntity(CreateEmployeeDto createEmployeeDto) {
        return EmployeeEntity.builder()
                .firstName(createEmployeeDto.getFirstName())
                .lastName(createEmployeeDto.getLastName())
                .username(createEmployeeDto.getUsername())
                .password(createEmployeeDto.getPassword())
                .role(createEmployeeDto.getRole())
                .build();
    }

    /**
     * From {@link EmployeeEntity} to {@link EmployeeDetailsDto}
     * @param employeeEntity {@link EmployeeEntity}
     * @return {@link EmployeeDetailsDto}
     */
    public static EmployeeDetailsDto fromEmployeeEntityToEmployeeDetailsDto(EmployeeEntity employeeEntity) {
        return EmployeeDetailsDto.builder()
                .employeeId(employeeEntity.getEmployeeId())
                .firstName(employeeEntity.getFirstName())
                .lastName(employeeEntity.getLastName())
                .username(employeeEntity.getUsername())
                .role(employeeEntity.getRole())
                .build();
    }

}
