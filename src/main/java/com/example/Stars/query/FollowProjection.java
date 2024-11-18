package com.example.Stars.query;

import com.example.Stars.api.UserFollowedEvent;
import com.example.Stars.read_model.FollowSummary;
import com.example.Stars.read_model.UserSummary;
import com.example.Stars.write_model.Follow;
import org.axonframework.commandhandling.CommandHandler;
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

    @QueryHandler
    public List<FollowSummary> getFollows(GetFollowsQuery query) {
        return mFollowSummaryRepository.findAll();
    }
}
