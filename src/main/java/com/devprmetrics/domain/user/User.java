package com.devprmetrics.domain.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    protected User() {}

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
        this.role = UserRole.REGULAR;
    }
}
