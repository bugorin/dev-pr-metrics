package com.devprmetrics.api.group;

import com.devprmetrics.domain.group.Group;
import java.util.List;

public record GroupApiResponse(Long id, String name, List<GroupMemberApiResponse> members) {

    public static GroupApiResponse from(Group group) {
        return new GroupApiResponse(
                group.getId(),
                group.getName(),
                group.getGroupMembers().stream().map(GroupMemberApiResponse::from).toList());
    }
}
