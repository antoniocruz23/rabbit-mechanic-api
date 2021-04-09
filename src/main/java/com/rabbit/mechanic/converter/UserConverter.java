package com.rabbit.mechanic.converter;


import com.rabbit.mechanic.command.user.CreateUserDto;
import com.rabbit.mechanic.command.user.UserDetailsDto;
import com.rabbit.mechanic.persistence.entity.UserEntity;

/**
 * User converter
 */
public class UserConverter {

    /**
     * From {@link CreateUserDto} to {@link UserEntity}
     * @param createUserDto {@link CreateUserDto}
     * @return {@link UserEntity}
     */
    public static UserEntity fromCreateUserDtoToUserEntity(CreateUserDto createUserDto) {
        return UserEntity.builder()
                .firstName(createUserDto.getFirstName())
                .lastName(createUserDto.getLastName())
                .address(createUserDto.getAddress())
                .email(createUserDto.getEmail())
                .cellNumber(createUserDto.getCellNumber())
                .password(createUserDto.getPassword())
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
