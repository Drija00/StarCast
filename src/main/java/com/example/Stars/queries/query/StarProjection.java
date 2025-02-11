package com.example.Stars.queries.query;

import com.example.Stars.DTOs.StarDTO;
import com.example.Stars.apis.api.StarDeletedEvent;
import com.example.Stars.apis.api.StarPostedEvent;
import com.example.Stars.apis.api.StarUpdatedEvent;
import com.example.Stars.converter.impl.StarConverter;
import com.example.Stars.queries.read_model.PageResult;
import com.example.Stars.queries.read_model.StarSummary;
import com.example.Stars.queries.read_model.UserSummary;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
    public PageResult on(GetUserStarsQuery gry){

        int offset = gry.getPageNumber() * gry.getPageSize();
        int limit = gry.getPageSize();

        Pageable pageable = PageRequest.of(offset, limit);
        List<StarSummary> items = mStarSummaryRepository.findAllByUserOrderByTimestampDesc(new UserSummary(gry.getUserId()),pageable).getContent();

        items.forEach(star -> Hibernate.initialize(star.getImages()));
        List<StarDTO> itemsDtos = items.stream().map(entity -> mStarConverter.toDto(entity)).collect(Collectors.toList());
        long totalItems = mStarSummaryRepository.countStarsForUsers(gry.getUserId());

        return new PageResult<>(itemsDtos, totalItems);
    }

    @QueryHandler
    public PageResult on(GetUserForYouStarsQuery gry){

        /*List<StarSummary> fystars = mStarSummaryRepository.findAllByUserOrderByTimestampDesc(new UserSummary(gry.getUserId()))
                .orElse(Collections.emptyList());

        fystars.forEach(star -> Hibernate.initialize(star.getImages()));
        List<StarDTO> starsDto = fystars.stream()
                .map(entity -> mStarConverter.toDto(entity))
                .collect(Collectors.toList());
        return starsDto;*/


        int offset = gry.getPageNumber() * gry.getPageSize();
        int limit = gry.getPageSize();

        Pageable pageable = PageRequest.of(offset, limit);
        List<StarSummary> items = mStarSummaryRepository.findAllStarsForUsersForYou(gry.getUserId(),pageable).getContent();
        items.forEach(star -> Hibernate.initialize(star.getImages()));
        List<StarDTO> itemsDtos = items.stream().map(entity -> mStarConverter.toDto(entity)).collect(Collectors.toList());
        long totalItems = mStarSummaryRepository.countStarsForUsersForYou(gry.getUserId());

        return new PageResult<>(itemsDtos, totalItems);

        //return mStarSummaryRepository.findAllStarsForUsersForYou(gry.getUserId()).orElse(null).stream().map(entity -> mStarConverter.toDto(entity))
                //.collect(Collectors.toList());
        //return null;
    }

    @QueryHandler
    public PageResult on(GetStarsQuery query) {
        int offset = query.getPageNumber() * query.getPageSize();
        int limit = query.getPageSize();

        Pageable pageable = PageRequest.of(offset, limit);
        List<StarSummary> items = mStarSummaryRepository.findAll(pageable).getContent();
        List<StarDTO> itemsDtos = items.stream().map(entity -> mStarConverter.toDto(entity)).collect(Collectors.toList());
        long totalItems = mStarSummaryRepository.count();

        return new PageResult<>(itemsDtos, totalItems);
    }

}
