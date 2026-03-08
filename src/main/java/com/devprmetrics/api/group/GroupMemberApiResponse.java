package com.devprmetrics.api.group;

import com.devprmetrics.domain.teammember.GroupMember;

public record GroupMemberApiResponse(String name, String imageUrl) {

    public static GroupMemberApiResponse from(GroupMember member) {
        return new GroupMemberApiResponse(member.getName(), "https://avatars.githubusercontent.com/u/" + member.getUser().getId());
    }
}
