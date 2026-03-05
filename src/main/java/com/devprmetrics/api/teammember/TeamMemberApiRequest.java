package com.devprmetrics.api.teammember;

import com.devprmetrics.domain.teammember.TeamMemberRole;
import jakarta.validation.constraints.NotNull;

public record TeamMemberApiRequest(
        @NotNull(message = "teamId is required") Long teamId,
        @NotNull(message = "userId is required") Long userId,
        @NotNull(message = "role is required") TeamMemberRole role) {
}
