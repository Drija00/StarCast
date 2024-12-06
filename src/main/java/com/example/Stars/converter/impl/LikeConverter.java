package com.example.Stars.converter.impl;

import com.example.Stars.DTOs.LikeDTO;
import com.example.Stars.converter.DtoEntityConverter;
import com.example.Stars.queries.read_model.LikeSummary;
import com.example.Stars.write_model.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LikeConverter implements DtoEntityConverter<LikeDTO, LikeSummary> {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private StarConverter starConverter;

    @Override
    public LikeDTO toDto(LikeSummary likeSummary) {
        return new LikeDTO(likeSummary.getLikeId(),userConverter.toDto(likeSummary.getUser()),starConverter.toDto(likeSummary.getStar()),likeSummary.getTimestamp());
    }

    @Override
    public LikeSummary toEntity(LikeDTO likeDTO) {
        return new LikeSummary(likeDTO.getLikeId(),userConverter.toEntity(likeDTO.getUser()),starConverter.toEntity(likeDTO.getStar()),likeDTO.getTimestamp());
    }
}