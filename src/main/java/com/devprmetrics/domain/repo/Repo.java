package com.devprmetrics.domain.repo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Repo {

    @Id
    private Long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    @Column(name = "html_url", length = 500)
    private String htmlUrl;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "default_branch", nullable = false, length = 255)
    private String defaultBranch;

    protected Repo() {
    }

    public Repo(Long id, String name, String fullName, String htmlUrl, boolean isPrivate, String defaultBranch) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.htmlUrl = htmlUrl;
        this.isPrivate = isPrivate;
        this.defaultBranch = defaultBranch;
    }

    public Repo merge(Repo other) {
        this.name = other.getName();
        this.fullName = other.getFullName();
        this.htmlUrl = other.getHtmlUrl();
        this.isPrivate = other.isPrivate();
        this.defaultBranch = other.getDefaultBranch();
        return this;
    }
}
