package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.DTOs.UserFollowDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.UserSummary;
import org.springframework.stereotype.Component;

@Component
public class UserFollowConverter implements DtoEntityConverter<UserFollowDTO, UserSummary> {
    @Override
    public UserFollowDTO toDto(UserSummary userSummary) {
        return new UserFollowDTO(userSummary.getUserId(),userSummary.getUsername(),userSummary.getFirstName(), userSummary.getLastName());
    }

    @Override
    public UserSummary toEntity(UserFollowDTO userDTO) {
        return new UserSummary(userDTO.getUserId(), userDTO.getUsername(), null,null,false, userDTO.getFirstName(), userDTO.getLastName(),null,null,null,null);
    }
}