package com.devprmetrics.api.team;

import jakarta.validation.constraints.NotBlank;

public record TeamApiRequest(@NotBlank(message = "team name is required") String name) {
}
