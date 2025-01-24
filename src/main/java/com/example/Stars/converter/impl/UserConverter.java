package com.example.Stars.converter.impl;


import com.example.Stars.DTOs.UserDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.UserSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserConverter implements DtoEntityConverter<UserDTO, UserSummary> {

    @Autowired
    UserFollowConverter followConverter;

    @Override
    public UserDTO toDto(UserSummary userSummary) {
        return new UserDTO(userSummary.getUserId(),userSummary.getUsername(),userSummary.getEmail(), userSummary.getPassword(), userSummary.isActive(), userSummary.getFirstName(), userSummary.getLastName(), userSummary.getJoinDate(), userSummary.getProfileImage(), userSummary.getBackgroundImage(), userSummary.getFollowing().stream().map(entity -> followConverter.toDto(entity))
                .collect(Collectors.toSet()));
    }

    @Override
    public UserSummary toEntity(UserDTO userDTO) {
        return new UserSummary(userDTO.getUserId(), userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(), userDTO.isActive(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getJoinDate(), userDTO.getProfileImage(), userDTO.getBackgroundImage());
    }

}
