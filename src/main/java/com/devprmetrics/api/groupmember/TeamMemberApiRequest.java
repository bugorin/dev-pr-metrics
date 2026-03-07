package com.devprmetrics.api.groupmember;

import com.devprmetrics.domain.teammember.TeamMemberRole;
import jakarta.validation.constraints.NotNull;

public record TeamMemberApiRequest(
        @NotNull(message = "groupId is required") Long groupId,
        @NotNull(message = "userId is required") Long userId,
        @NotNull(message = "role is required") TeamMemberRole role) {
}
