package com.example.Stars.queries.query;

import com.example.Stars.queries.read_model.StarSummary;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StarJavaRepo extends JpaRepository<StarSummary, UUID> {

    @Query("SELECT s FROM StarSummary s LEFT JOIN FETCH s.likes")
    List<StarSummary> findAll();
}
