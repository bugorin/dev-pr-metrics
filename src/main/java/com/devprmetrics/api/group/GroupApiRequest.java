package com.devprmetrics.api.group;

import jakarta.validation.constraints.NotBlank;

public record GroupApiRequest(@NotBlank(message = "group name is required") String name) {
}
