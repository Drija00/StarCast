package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.StarSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StarConverter implements DtoEntityConverter<StarDTO, StarSummary> {

    @Autowired
    private UserConverter userConverter;

    @Override
    public StarDTO toDto(StarSummary starSummary) {
        return new StarDTO(starSummary.getStarId(),starSummary.getContent(), userConverter.toDto(starSummary.getUser()),starSummary.getTimestamp());
    }

    @Override
    public StarSummary toEntity(StarDTO starDTO) {
        return new StarSummary(starDTO.getStarId(),starDTO.getContent(),userConverter.toEntity(starDTO.getUser()),starDTO.getTimestamp());
    }

}
