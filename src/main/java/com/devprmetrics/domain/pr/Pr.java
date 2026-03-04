package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
public class Pr {

    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_author_id", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_repository_id", nullable = false)
    private Repo repository;

    @Enumerated(EnumType.STRING)
    @Column(name = "github_status", nullable = false, length = 20)
    private PrStatus githubStatus;

    @Column(name = "github_title", length = 255)
    private String githubTitle;

    @Column(name = "github_created_at", nullable = false)
    private LocalDateTime githubCreatedAt;

    @Column(name = "github_updated_at", nullable = false)
    private LocalDateTime githubUpdatedAt;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", name = "infos")
    private PrInfo info = new PrInfo();

    @JsonManagedReference
    @OneToMany(mappedBy = "pr", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Reviewer> reviewers = new ArrayList<>();

    protected Pr() {
    }

    public Pr(Long id, User author, Repo repository, PrStatus githubStatus,
              String githubTitle, String githubHtmlUrl, int githubAdditions,
              int githubDeletions, int githubFileChanges, int githubOpenReviewComments,
              LocalDateTime githubCreatedAt, LocalDateTime githubUpdatedAt) {
        this.id = id;
        this.author = author;
        this.repository = repository;
        this.githubStatus = githubStatus;
        this.githubTitle = githubTitle;
        this.info = new PrInfo(
                githubHtmlUrl,
                githubAdditions,
                githubDeletions,
                githubFileChanges,
                githubOpenReviewComments
        );
        this.githubCreatedAt = githubCreatedAt;
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public Pr merge(Pr pr) {
        this.setRepository(pr.getRepository());
        this.setGithubStatus(pr.getGithubStatus());
        this.setGithubTitle(pr.getGithubTitle());
        this.info = new PrInfo(
                pr.getGithubHtmlUrl(),
                pr.getGithubAdditions(),
                pr.getGithubDeletions(),
                pr.getGithubFileChanges(),
                pr.getGithubOpenReviewComments()
        );
        this.setGithubCreatedAt(pr.getGithubCreatedAt());
        this.setGithubUpdatedAt(pr.getGithubUpdatedAt());

        for (Reviewer reviewer : pr.getReviewers()) {
            this.addOrUpdate(reviewer);
        }

        return this;
    }

    public int getGithubOpenReviewComments() {
        return this.info.getGithubOpenReviewComments();
    }

    public int getGithubFileChanges() {
        return this.info.getGithubFileChanges();
    }

    public int getGithubDeletions() {
        return this.info.getGithubDeletions();
    }

    public int getGithubAdditions() {
        return this.info.getGithubAdditions();
    }

    public String getGithubHtmlUrl() {
        return this.info.getGithubHtmlUrl();
    }

    public void addOrUpdate(Reviewer reviewer) {
        Optional<Reviewer> existing = reviewers.stream()
                .filter(r -> r.isUser(reviewer.getUser()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().merge(reviewer);
            return;
        }

        this.reviewers.add(reviewer);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PrInfo implements Serializable {
        private String githubHtmlUrl;
        private int githubAdditions;
        private int githubDeletions;
        private int githubFileChanges;
        private int githubOpenReviewComments;
    }
}
