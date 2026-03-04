package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.user.User;
import org.kohsuke.github.GHUser;

public record UserEntityMapper() {

    public static User mapper(GHUser ghUser) {
        try {
            if (ghUser == null) {
                throw new IllegalArgumentException("GHUser is null");
            }

            return new User(
                    ghUser.getId(),
                    ghUser.getName()
            );
        }catch (Exception e) {
            throw new IllegalArgumentException("Could not map GHUser to user entity", e);
        }
    }
}
