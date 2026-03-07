package com.devprmetrics.api.groupmember;

import com.devprmetrics.api.group.GroupApiResponse;
import com.devprmetrics.api.user.UserApiResponse;
import com.devprmetrics.domain.teammember.GroupMember;
import com.devprmetrics.domain.teammember.TeamMemberRole;

public record TeamMemberApiResponse(Long id, GroupApiResponse team, UserApiResponse user, TeamMemberRole role) {

    public static TeamMemberApiResponse from(GroupMember groupMember) {
        return new TeamMemberApiResponse(
                groupMember.getId(),
                GroupApiResponse.from(groupMember.getGroup()),
                UserApiResponse.from(groupMember.getUser()),
                groupMember.getRole());
    }
}
