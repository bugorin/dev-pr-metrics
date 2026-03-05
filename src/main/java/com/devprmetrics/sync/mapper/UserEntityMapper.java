package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.user.User;
import org.kohsuke.github.GHUser;

import java.util.Optional;

public record UserEntityMapper() {

    public static User mapper(GHUser ghUser) {
        try {
            if (ghUser == null) {
                throw new IllegalArgumentException("GHUser is null");
            }

            return new User(
                    ghUser.getId(),
                    filterString(ghUser.getName()).orElse("github-user-" + ghUser.getId()),
                    filterString(ghUser.getLogin()).orElse("user-" + ghUser.getId())
            );
        }catch (Exception e) {
            throw new IllegalArgumentException("Could not map GHUser to user entity", e);
        }
    }

    private static Optional<String> filterString(String name) {
        return Optional.ofNullable(name)
                .filter(s -> !s.isBlank());
    }
}
