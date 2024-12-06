package com.example.Stars.converter.impl;


import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.UserSummary;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements DtoEntityConverter<UserDTO, UserSummary> {
    @Override
    public UserDTO toDto(UserSummary userSummary) {
        return new UserDTO(userSummary.getUserId(),userSummary.getUsername(),userSummary.getEmail(), userSummary.getPassword(), userSummary.isActive());
    }

    @Override
    public UserSummary toEntity(UserDTO userDTO) {
        return new UserSummary(userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.isActive());
    }

}
