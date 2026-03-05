package com.devprmetrics.api.team;

import com.devprmetrics.domain.team.Team;

public record TeamApiResponse(Long id, String name) {

    public static TeamApiResponse from(Team team) {
        return new TeamApiResponse(team.getId(), team.getName());
    }
}
