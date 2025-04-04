//package com.example.Stars.queries.query;
//
//import com.example.Stars.DTOs.FollowDTO;
//import com.example.Stars.apis.api.UserFollowedEvent;
//import com.example.Stars.apis.api.UserUnfollowedEvent;
//import com.example.Stars.converter.impl.FollowConverter;
//import com.example.Stars.queries.read_model.FollowSummary;
//import com.example.Stars.queries.read_model.UserSummary;
//import org.axonframework.config.ProcessingGroup;
//import org.axonframework.eventhandling.EventHandler;
//import org.axonframework.queryhandling.QueryHandler;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@Profile("query_follow")
//@ProcessingGroup("followProcessor")
//public class FollowProjection {
//    private final FollowSummaryRepository mFollowSummaryRepository;
//    private final FollowConverter mFollowConverter;
//
//    public FollowProjection(FollowSummaryRepository mFollowSummaryRepository, FollowConverter mFollowConverter) {
//        this.mFollowSummaryRepository = mFollowSummaryRepository;
//        this.mFollowConverter = mFollowConverter;
//    }
//
//    @EventHandler
//    public void handle(UserFollowedEvent event) {
//        FollowSummary f = new FollowSummary(
//                event.getFollowId(),
//                new UserSummary(event.getFollowerId()),
//                new UserSummary(event.getFolloweeId()),
//                event.getTimestamp()
//        );
//        mFollowSummaryRepository.save(f);
//    }
//
//    @EventHandler
//    public void handle(UserUnfollowedEvent event) {
//        mFollowSummaryRepository.deleteById(event.getFollowId());
//    }
//
//    @QueryHandler
//    public List<FollowDTO> getFollows(GetFollowsQuery query) {
//        return mFollowSummaryRepository.findAll().stream().map(entity -> mFollowConverter.toDto(entity))
//                .collect(Collectors.toList());
//    }
//
//    @QueryHandler
//    public FollowDTO getFollows(GetFollowQuery query) {
//        FollowSummary fs = mFollowSummaryRepository.findByFollowerAndFollowee(new UserSummary(query.getFollower_id()),new UserSummary(query.getFollowee_id())).orElseThrow(() -> new RuntimeException("Follow not found"));
//        return mFollowConverter.toDto(fs);
//    }
//}
