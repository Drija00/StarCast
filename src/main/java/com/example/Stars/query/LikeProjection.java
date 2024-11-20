package com.example.Stars.query;

import com.example.Stars.api.StarLikedEvent;
import com.example.Stars.api.StarUnlikedEvent;
import com.example.Stars.read_model.LikeSummary;
import com.example.Stars.read_model.StarSummary;
import com.example.Stars.read_model.UserSummary;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeProjection {

    private final LikeRepository mLikeRepository;

    public LikeProjection(LikeRepository mLikeRepository) {
        this.mLikeRepository = mLikeRepository;
    }

    @EventHandler
    public void handle(StarLikedEvent event) {
        LikeSummary like = new LikeSummary(
            event.getLikeId(),
            new UserSummary(event.getUserId()),
            new StarSummary(event.getStarId()),
            event.getTimestamp()
        );
        mLikeRepository.save(like);
    }

    @EventHandler
    public void handle(StarUnlikedEvent evt){
        mLikeRepository.deleteById(evt.getLikeId());
    }

    @QueryHandler
    public List<LikeSummary> getLikes(GetLikesQuery query) {
        return mLikeRepository.findAll();
    }

    @QueryHandler
    public LikeSummary getLikeByUserAndStar(GetLikeQuery query) {
        return mLikeRepository.findByUserAndStar(new UserSummary(query.getUser_id()),new StarSummary(query.getStar_id())).orElseThrow(() -> new RuntimeException("No such star"));
    }
}
