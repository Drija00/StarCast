package com.example.Stars.queries.query;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.apis.api.StarDeletedEvent;
import com.example.Stars.apis.api.StarPostedEvent;
import com.example.Stars.apis.api.StarUpdatedEvent;
import com.example.Stars.converter.impl.StarConverter;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.queries.read_model.UserSummary;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Profile("query")
public class StarProjection {
    private final StarSummaryRepository mStarSummaryRepository;
    private final StarConverter mStarConverter;

    public StarProjection(StarSummaryRepository mStarSummaryRepository, StarConverter mStarConverter) {
        this.mStarSummaryRepository = mStarSummaryRepository;
        this.mStarConverter = mStarConverter;
    }

    @EventHandler
    public void on(StarPostedEvent event) {
        StarSummary star = new StarSummary(
                event.getStarId(),
                event.getContent(),
                new UserSummary(event.getUserId()),
                event.getTimestamp()
        );
        mStarSummaryRepository.save(star);
    }

    @EventHandler
    public void on(StarDeletedEvent event) {
        mStarSummaryRepository.deleteById(event.getStarId());
    }

    @EventHandler
    public void on(StarUpdatedEvent event) {
        StarSummary star = new StarSummary(
                event.getStarId(),
                event.getContent(),
                new UserSummary(event.getUser().getUser_id()),
                event.getTimestamp()
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
        return mStarSummaryRepository.findAllStarsForUsersForYou(gry.getUserId()).orElse(null).stream().map(entity -> mStarConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @QueryHandler
    public List<StarDTO> on(GetStarsQuery qry) {
        try {
            return mStarSummaryRepository.findAll().stream().map(entity -> mStarConverter.toDto(entity))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Failed to fetch stars", e);
        }
    }
}
