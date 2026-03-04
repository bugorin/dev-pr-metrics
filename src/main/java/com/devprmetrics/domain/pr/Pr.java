package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.User;
import jakarta.persistence.*;
import org.kohsuke.github.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "pr")
    private List<Reviewer> reviewers = new ArrayList<>();

    protected Pr() {
    }

    public Pr(int id, User author, PrStatus githubStatus,
               Date githubCreatedAt, Date githubUpdatedAt,
               String infos) {
        this.id = (long) id;
        this.author = author;
        this.githubStatus = githubStatus;
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

    public Pr merge(GHPullRequest ghPullRequest) throws IOException {
        this.setGithubStatus(ghPullRequest.getState());
        this.setGithubCreatedAt(ghPullRequest.getCreatedAt());
        this.setGithubUpdatedAt(ghPullRequest.getUpdatedAt());
        return this;
    }

    public void addReviews(User user, GHPullRequestReview reviewer) throws IOException {
        this.addReviews(
                user,
                ReviewerStatus.from(reviewer.getState()),
                toLocalDateTime(reviewer.getSubmittedAt())
        );
    }

    public void addReviews(User user, ReviewerStatus status, LocalDateTime submittedAt) {
        this.reviewers.add(new Reviewer(this, user, status, submittedAt));
    }
}
