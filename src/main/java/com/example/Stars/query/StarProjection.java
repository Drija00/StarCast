package com.example.Stars.query;

import com.example.Stars.api.StarPostedEvent;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StarProjection {
    private final StarSummaryRepository mStarSummaryRepository;

    public StarProjection(StarSummaryRepository mStarSummaryRepository) {
        this.mStarSummaryRepository = mStarSummaryRepository;
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

    @QueryHandler
    public List<StarSummary> on(GetStarsQuery qry) {
        try {
            List<StarSummary> stars = mStarSummaryRepository.findAll();
            System.out.println(stars.toString());
            return stars;
        } catch (Exception e) {
            // Log the exception for further inspection
            System.out.println(e.getMessage());
            // Handle the exception, possibly returning an empty list or throwing a custom exception
            throw new RuntimeException("Failed to fetch stars", e);
        }
    }
}
