package com.example.Stars.queries.query;

import com.example.Stars.apis.api.UserFollowedEvent;
import com.example.Stars.apis.api.UserUnfollowedEvent;
import com.example.Stars.queries.read_model.FollowSummary;
import com.example.Stars.queries.read_model.UserSummary;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowProjection {
    private final FollowSummaryRepository mFollowSummaryRepository;

    public FollowProjection(FollowSummaryRepository mFollowSummaryRepository) {
        this.mFollowSummaryRepository = mFollowSummaryRepository;
    }

    @EventHandler
    public void handle(UserFollowedEvent event) {
        FollowSummary f = new FollowSummary(
                event.getFollowId(),
                new UserSummary(event.getFollowerId()),
                new UserSummary(event.getFolloweeId()),
                event.getTimestamp()
        );
        mFollowSummaryRepository.save(f);
    }

    @EventHandler
    public void handle(UserUnfollowedEvent event) {
        mFollowSummaryRepository.deleteById(event.getFollowId());
    }

    @QueryHandler
    public List<FollowSummary> getFollows(GetFollowsQuery query) {
        return mFollowSummaryRepository.findAll();
    }

    @QueryHandler
    public FollowSummary getFollows(GetFollowQuery query) {
        return mFollowSummaryRepository.findByFollowerAndFollowee(new UserSummary(query.getFollower_id()),new UserSummary(query.getFollowee_id())).orElseThrow(() -> new RuntimeException("Follow not found"));
    }
}
