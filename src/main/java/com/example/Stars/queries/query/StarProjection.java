package com.example.Stars.queries.query;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.apis.api.StarDeletedEvent;
import com.example.Stars.apis.api.StarPostedEvent;
import com.example.Stars.apis.api.StarUpdatedEvent;
import com.example.Stars.converter.impl.StarConverter;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.queries.read_model.UserSummary;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Profile("query_star")
@ProcessingGroup("starProcessor")
public class StarProjection {
    private final StarSummaryRepository mStarSummaryRepository;
    private final StarConverter mStarConverter;
    private StarJavaRepo mStarJavaRepo;
    private final UserSummaryRepository mUserSummaryRepository;

    public StarProjection(StarSummaryRepository mStarSummaryRepository, StarConverter mStarConverter, StarJavaRepo mStarJavaRepo, UserSummaryRepository mUserSummaryRepository) {
        this.mStarSummaryRepository = mStarSummaryRepository;
        this.mStarConverter = mStarConverter;
        this.mStarJavaRepo = mStarJavaRepo;
        this.mUserSummaryRepository = mUserSummaryRepository;
    }

    @EventHandler
    public void on(StarPostedEvent event) {
        Optional<UserSummary> user = mUserSummaryRepository.findById(event.getUserId());

        if (user.isEmpty()) {
            System.err.println("User not found for event: " + event.getStarId() + ". Skipping post.");
            return;
        }

        StarSummary star = new StarSummary(
                event.getStarId(),
                event.getContent(),
                new UserSummary(event.getUserId()),
                event.getTimestamp(),
                null,
                event.getImages()
        );
        mStarSummaryRepository.save(star);
    }

    @EventHandler
    public void on(StarDeletedEvent event) {
        mStarSummaryRepository.deleteById(event.getStarId());
    }

    @EventHandler
    public void on(StarUpdatedEvent event) {
        StarSummary s = mStarSummaryRepository.findByStarId(event.getStarId()).orElseThrow(() -> new RuntimeException("Star not found"));
        StarSummary star = new StarSummary(
                event.getStarId(),
                event.getContent(),
                new UserSummary(event.getUser().getUser_id()),
                event.getTimestamp(),
                s.getLikes(),
                s.getImages()
        );
        mStarSummaryRepository.save(star);
    }

    @QueryHandler
    public StarDTO on(GetStarQuery gry){
        StarSummary ss = mStarSummaryRepository.findByStarId(gry.getStar_id()).orElse(null);
        return ss!=null?mStarConverter.toDto(ss):null;
    }

    @QueryHandler
    public List<StarDTO> on(GetUserStarsQuery gry){
        return mStarSummaryRepository.findAllByUserOrderByTimestampDesc(new UserSummary(gry.getUserId())).orElse(null).stream().map(entity -> mStarConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @QueryHandler
    public List<StarDTO> on(GetUserForYouStarsQuery gry){
//        return mStarSummaryRepository.findAllStarsForUsersForYou(gry.getUserId()).orElse(null).stream().map(entity -> mStarConverter.toDto(entity))
//                .collect(Collectors.toList());
        return null;
    }

    @QueryHandler
    public List<StarDTO> on(GetStarsQuery qry) {
        try {
            List<StarSummary> stars = mStarSummaryRepository.findAll();
            List<StarDTO> collect = new ArrayList<>();
            for (StarSummary entity : stars) {
                StarDTO dto = mStarConverter.toDto(entity);
                collect.add(dto);
            }
            return collect;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch stars", e);
        }
    }
}
