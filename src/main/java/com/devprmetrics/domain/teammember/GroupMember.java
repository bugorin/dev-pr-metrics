package com.devprmetrics.domain.teammember;

import com.devprmetrics.domain.group.Group;
import com.devprmetrics.domain.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private TeamMemberRole role;

    protected GroupMember() {
    }

    public GroupMember(Group group, User user, TeamMemberRole role) {
        this.group = group;
        this.user = user;
        this.role = role;
    }

    public String getName() {
        return user.getName();
    }
}
