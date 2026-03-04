package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import org.kohsuke.github.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.devprmetrics.util.LocalDateTimeUtils.toLocalDateTime;

@Entity
public class Pr {

    @Id
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_author_id", nullable = false)
    private User author;

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

    protected Pr() {
    }

    public Pr(int id, User author, GHIssueState githubStatus,
               Date githubCreatedAt, Date githubUpdatedAt,
               String infos) {
        this.id = (long) id;
        this.author = author;
        this.githubStatus = PrStatus.from(githubStatus);
        this.githubCreatedAt = toLocalDateTime(githubCreatedAt);
        this.githubUpdatedAt = toLocalDateTime(githubUpdatedAt);
        this.infos = infos;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public PrStatus getGithubStatus() {
        return githubStatus;
    }

    public void setGithubStatus(GHIssueState githubStatus) {
        this.githubStatus = PrStatus.from(githubStatus);
    }

    public LocalDateTime getGithubCreatedAt() {
        return githubCreatedAt;
    }

    public void setGithubCreatedAt(LocalDateTime githubCreatedAt) {
        this.githubCreatedAt = githubCreatedAt;
    }

    public void setGithubCreatedAt(Date githubCreatedAt) {
        this.setGithubCreatedAt(toLocalDateTime(githubCreatedAt));
    }

    public LocalDateTime getGithubUpdatedAt() {
        return githubUpdatedAt;
    }

    public void setGithubUpdatedAt(LocalDateTime githubUpdatedAt) {
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public void setGithubUpdatedAt(Date githubUpdatedAt) {
        this.setGithubUpdatedAt(toLocalDateTime(githubUpdatedAt));
    }

    public List<Reviewer> getReviewers() {
        return reviewers;
    }

    public String getInfos() {
        return infos;
    }

    public void setInfos(String infos) {
        this.infos = infos;
    }

    public Pr merge(GHPullRequest ghPullRequest, Map<User, GHPullRequestReview> reviewByUser) throws IOException {
        this.setGithubStatus(ghPullRequest.getState());
        this.setGithubCreatedAt(ghPullRequest.getCreatedAt());
        this.setGithubUpdatedAt(ghPullRequest.getUpdatedAt());

        for (Map.Entry<User, GHPullRequestReview> key : reviewByUser.entrySet()) {
            this.addOrUpdate(key.getKey(), key.getValue());
        }

        return this;
    }

    public void addOrUpdate(User user, GHPullRequestReview reviewer) throws IOException {
        ReviewerStatus status = ReviewerStatus.from(reviewer.getState());
        LocalDateTime submittedAt = toLocalDateTime(reviewer.getSubmittedAt());
        for (Reviewer existing : reviewers) {
            if (existing.getUser().getId().equals(user.getId())) {
                existing.setStatus(status);
                existing.setSubmittedAt(submittedAt);
                return;
            }
        }
        this.reviewers.add(new Reviewer(this, user, status, submittedAt));
    }
}
