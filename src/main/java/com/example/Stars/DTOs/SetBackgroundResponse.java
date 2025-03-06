package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SetBackgroundResponse {
    private String backgroundImage;

    public SetBackgroundResponse(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
