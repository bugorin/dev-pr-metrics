package com.devprmetrics.api.group;

import com.devprmetrics.domain.group.Group;

public record GroupApiResponse(Long id, String name) {

    public static GroupApiResponse from(Group group) {
        return new GroupApiResponse(group.getId(), group.getName());
    }
}
