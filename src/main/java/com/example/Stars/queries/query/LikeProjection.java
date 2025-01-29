package com.example.Stars.queries.query;

import com.example.Stars.DTOs.LikeDTO;
import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.apis.api.StarLikedEvent;
import com.example.Stars.apis.api.StarUnlikedEvent;
import com.example.Stars.converter.impl.LikeConverter;
import com.example.Stars.queries.read_model.LikeSummary;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.queries.read_model.UserSummary;
import jakarta.transaction.Transactional;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Profile("query_like")
@ProcessingGroup("likeProcessor")
public class LikeProjection {

    private final LikeRepository mLikeRepository;
    private final LikeConverter mLikeConverter;
    private final UserSummaryRepository mUserSummaryRepository;

    public LikeProjection(LikeRepository mLikeRepository, LikeConverter mLikeConverter, UserSummaryRepository mUserSummaryRepository) {
        this.mLikeRepository = mLikeRepository;
        this.mLikeConverter = mLikeConverter;
        this.mUserSummaryRepository = mUserSummaryRepository;
    }

    @EventHandler
    public void handle(StarLikedEvent event) {
        Optional<UserSummary> user = mUserSummaryRepository.findById(event.getUserId());

        if (user.isEmpty()) {
            System.err.println("User not found for event: " + event.getStarId() + ". Skipping post.");
            return;
        }
        LikeSummary like = new LikeSummary(
            event.getLikeId(),
            new UserSummary(event.getUserId()),
            new StarSummary(event.getStarId()),
            event.getTimestamp(),
            event.getActive()
        );
        mLikeRepository.save(like);
    }

    @EventHandler
    public void handle(StarUnlikedEvent evt){
        mLikeRepository.deleteById(evt.getLikeId());
    }

    @Transactional
    @QueryHandler
    public List<LikeDTO> getLikes(GetLikesQuery query) {
        List<LikeSummary> l1 = mLikeRepository.findAll();
        l1.forEach(like-> Hibernate.initialize(like.getStar().getImages()));
        List<LikeDTO> ldtos = new ArrayList<>();
        ldtos = l1.stream().map(entity -> mLikeConverter.toDto(entity)).collect(Collectors.toList());
        return ldtos;
    }

    @QueryHandler
    public LikeDTO getLikeByUserAndStar(GetLikeQuery query) {
        LikeSummary ls = mLikeRepository.findByUserAndStar(new UserSummary(query.getUser_id()),new StarSummary(query.getStar_id())).orElseThrow(() -> new RuntimeException("No such star"));
        return ls!=null?mLikeConverter.toDto(ls):null;
    }

    @QueryHandler
    public List<LikeDTO> getLikesByStar(GetStarLikesQuery query) {
        return mLikeRepository.findAllByStar(new StarSummary(query.getStar_id())).orElseThrow(() -> new RuntimeException("No such star")).stream().map(entity -> mLikeConverter.toDto(entity))
                .collect(Collectors.toList());
    }
}
