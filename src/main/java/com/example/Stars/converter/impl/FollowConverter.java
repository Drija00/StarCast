//package com.example.Stars.converter.impl;
//
//import com.example.Stars.DTOs.FollowDTO;
//import com.example.Stars.converter.DtoEntityConverter;
//import com.example.Stars.queries.read_model.FollowSummary;
//import com.example.Stars.write_model.Follow;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FollowConverter implements DtoEntityConverter<FollowDTO, FollowSummary> {
//
//    @Autowired
//    private UserFollowConverter userConverter;
//
//    @Override
//    public FollowDTO toDto(FollowSummary followSummary) {
//        return new FollowDTO(followSummary.getFollowId(), userConverter.toDto(followSummary.getFollower()), userConverter.toDto(followSummary.getFollowee()), followSummary.getTimestamp());
//    }
//
//    @Override
//    public FollowSummary toEntity(FollowDTO followDTO) {
//        return new FollowSummary(followDTO.getFollowId(),userConverter.toEntity(followDTO.getFollower()),userConverter.toEntity(followDTO.getFollowee()),followDTO.getTimestamp());
//    }
//}
