package com.devprmetrics.api.teammember;

import com.devprmetrics.api.team.TeamApiResponse;
import com.devprmetrics.api.user.UserApiResponse;
import com.devprmetrics.domain.teammember.TeamMember;
import com.devprmetrics.domain.teammember.TeamMemberRole;

public record TeamMemberApiResponse(Long id, TeamApiResponse team, UserApiResponse user, TeamMemberRole role) {

    public static TeamMemberApiResponse from(TeamMember teamMember) {
        return new TeamMemberApiResponse(
                teamMember.getId(),
                TeamApiResponse.from(teamMember.getTeam()),
                UserApiResponse.from(teamMember.getUser()),
                teamMember.getRole());
    }
}
