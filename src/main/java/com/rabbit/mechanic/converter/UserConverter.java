package com.rabbit.mechanic.converter;

import com.rabbit.mechanic.command.user.CreateOrUpdateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;
import com.rabbit.mechanic.persistence.entity.UserEntity;

/**
 * User converter
 */
public class UserConverter {

    /**
     * From {@link CreateOrUpdateUserDto} to {@link UserEntity}
     * @param createOrUpdateUserDto {@link CreateOrUpdateUserDto}
     * @return {@link UserEntity}
     */
    public static UserEntity fromCreateUserDtoToUserEntity(CreateOrUpdateUserDto createOrUpdateUserDto) {
        return UserEntity.builder()
                .firstName(createOrUpdateUserDto.getFirstName())
                .lastName(createOrUpdateUserDto.getLastName())
                .address(createOrUpdateUserDto.getAddress())
                .email(createOrUpdateUserDto.getEmail())
                .cellNumber(createOrUpdateUserDto.getCellNumber())
                .build();
    }

    /**
     * From {@link UserEntity} to {@link UserDetailsDto}
     * @param userEntity {@link UserEntity}
     * @return {@link UserDetailsDto}
     */
    public static UserDetailsDto fromUserEntityToUserDetailsDto(UserEntity userEntity) {
        return UserDetailsDto.builder()
                .userId(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .address(userEntity.getAddress())
                .email(userEntity.getEmail())
                .cellNumber(userEntity.getCellNumber())
                .build();
    }

}
