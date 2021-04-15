package com.rabbit.mechanic.converter;

import com.rabbit.mechanic.command.customer.CreateOrUpdateCustomerDto;
import com.rabbit.mechanic.command.customer.CustomerDetailsDto;
import com.rabbit.mechanic.persistence.entity.CustomerEntity;

/**
 * Customer converter
 */
public class CustomerConverter {

    /**
     * From {@link CreateOrUpdateCustomerDto} to {@link CustomerEntity}
     * @param createOrUpdateCustomerDto {@link CreateOrUpdateCustomerDto}
     * @return {@link CustomerEntity}
     */
    public static CustomerEntity fromCreateOrUpdateCustomerDtoToCustomerEntity(CreateOrUpdateCustomerDto createOrUpdateCustomerDto) {
        return CustomerEntity.builder()
                .firstName(createOrUpdateCustomerDto.getFirstName())
                .lastName(createOrUpdateCustomerDto.getLastName())
                .address(createOrUpdateCustomerDto.getAddress())
                .email(createOrUpdateCustomerDto.getEmail())
                .cellNumber(createOrUpdateCustomerDto.getCellNumber())
                .build();
    }

    /**
     * From {@link CustomerEntity} to {@link CustomerDetailsDto}
     * @param customerEntity {@link CustomerEntity}
     * @return {@link CustomerDetailsDto}
     */
    public static CustomerDetailsDto fromCustomerEntityToCustomerDetailsDto(CustomerEntity customerEntity) {
        return CustomerDetailsDto.builder()
                .customerId(customerEntity.getCustomerId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .address(customerEntity.getAddress())
                .email(customerEntity.getEmail())
                .cellNumber(customerEntity.getCellNumber())
                .build();
    }
}
