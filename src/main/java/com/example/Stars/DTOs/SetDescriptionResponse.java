package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SetDescriptionResponse {
    private String description;

    public SetDescriptionResponse(String description) {
        this.description = description;
    }
}
