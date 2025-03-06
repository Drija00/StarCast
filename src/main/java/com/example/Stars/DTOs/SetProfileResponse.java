package com.example.Stars.DTOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SetProfileResponse {
    private String profileImage;

    public SetProfileResponse(String profileImage) {
        this.profileImage = profileImage;
    }
}
