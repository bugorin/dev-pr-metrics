package com.devprmetrics.domain.group;

import com.devprmetrics.domain.teammember.GroupMember;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`Group`")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();

    protected Group() {
    }

    public Group(String name) {
        this.name = name;
    }
}
