package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Getter
@Setter
public class StarPostDTO {
    private UUID user_id;
    private String content;
    private Set<String> images = new HashSet<>();

    public StarPostDTO(UUID user_id) {
        this.user_id = user_id;
    }

    public StarPostDTO(UUID user_id, String content, Set<String> images) {
        this.user_id = user_id;
        this.content = content;
        this.images = images;
    }

    @Override
    public String toString() {
        return "StarPostDTO{" +
                "user_id=" + user_id +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }
}
