package com.devprmetrics.api.user;

import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserRole;

public record UserApiResponse(Long id, String name, UserRole role, String ghImage, String ghUrl) {
    public static UserApiResponse from(User user) {
        return new UserApiResponse(
                user.getId(),
                user.getName(),
                user.getRole(),
                "https://avatars.githubusercontent.com/u/" + user.getId(),
                "https://github.com/" + user.getUsername().toLowerCase());
    }
}
