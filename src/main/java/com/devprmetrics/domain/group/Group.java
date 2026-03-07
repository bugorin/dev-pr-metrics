package com.devprmetrics.domain.group;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "`Group`")
@Data
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 255, unique = true)
    private String name;

    protected Group() {
    }

    public Group(String name) {
        this.name = name;
    }
}
