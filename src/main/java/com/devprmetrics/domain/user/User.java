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

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "is_bot", nullable = false)
    private boolean bot;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    protected User() {}

    public User(Long id, String name, String username, boolean bot) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.bot = bot;
        this.role = UserRole.REGULAR;
    }
}
