package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.StarSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StarConverter implements DtoEntityConverter<StarDTO, StarSummary> {

    @Autowired
    private UserFollowConverter userConverter;

    @Override
    public StarDTO toDto(StarSummary starSummary) {
        return new StarDTO(starSummary.getStarId(),starSummary.getContent(), userConverter.toDto(starSummary.getUser()),starSummary.getTimestamp(),starSummary.getLikes().stream().map(entity -> userConverter.toDto(entity.getUser()))
                .collect(Collectors.toSet()),starSummary.getImages());
    }

    @Override
    public StarSummary toEntity(StarDTO starDTO) {
        return new StarSummary(starDTO.getStarId(),starDTO.getContent(),userConverter.toEntity(starDTO.getUser()),starDTO.getTimestamp(), null,starDTO.getImages());
    }


}
