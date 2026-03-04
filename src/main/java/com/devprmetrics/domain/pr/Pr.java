package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Pr {

    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_author_id", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_repository_id", nullable = true)
    private Repo repository;

    @Enumerated(EnumType.STRING)
    @Column(name = "github_status", nullable = false, length = 20)
    private PrStatus githubStatus;

    @Column(name = "github_created_at", nullable = false)
    private LocalDateTime githubCreatedAt;

    @Column(name = "github_updated_at", nullable = false)
    private LocalDateTime githubUpdatedAt;

    @Column(name = "infos", columnDefinition = "json")
    private String infos;

    @JsonManagedReference
    @OneToMany(mappedBy = "pr", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reviewer> reviewers = new ArrayList<>();

    protected Pr() {}

    public Pr(Long id, User author, Repo repository, PrStatus githubStatus,
               LocalDateTime githubCreatedAt, LocalDateTime githubUpdatedAt) {
        this.id = id;
        this.author = author;
        this.repository = repository;
        this.githubStatus = githubStatus;
        this.githubCreatedAt = githubCreatedAt;
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public Pr merge(Pr pr) {
        this.setRepository(pr.getRepository());
        this.setGithubStatus(pr.getGithubStatus());
        this.setGithubCreatedAt(pr.getGithubCreatedAt());
        this.setGithubUpdatedAt(pr.getGithubUpdatedAt());

        for(Reviewer reviewer : pr.getReviewers()) {
            this.addOrUpdate(reviewer);
        }

        return this;
    }

    public void addOrUpdate(Reviewer reviewer) {
        Optional<Reviewer> existing = reviewers.stream()
                .filter(r -> r.isUser(reviewer.getUser()))
                .findFirst();

        if(existing.isPresent()) {
            existing.get().merge(reviewer);
            return;
        }

        this.reviewers.add(reviewer);
    }
}
