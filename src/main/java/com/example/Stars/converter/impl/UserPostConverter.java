package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.DTOs.UserPostDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.UserSummary;

public class UserPostConverter implements DtoEntityConverter<UserPostDTO, UserSummary> {
    @Override
    public UserPostDTO toDto(UserSummary userSummary) {
        return new UserPostDTO(userSummary.getUsername(),userSummary.getEmail(), userSummary.getPassword(), userSummary.isActive());
    }

    @Override
    public UserSummary toEntity(UserPostDTO userDTO) {
        return new UserSummary(null,userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.isActive());
    }
}
