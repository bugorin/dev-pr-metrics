package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserCreateService;
import jakarta.persistence.*;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.devprmetrics.domain.pr.PrStatus.from;
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

    protected Pr() {
    }

    public static Pr createFrom(GHPullRequest ghPullRequest, UserCreateService userCreateService) throws IOException {
        User author = userCreateService.findOrCreated(ghPullRequest.getUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new Pr(
                (long) ghPullRequest.getNumber(),
                author,
                PrStatus.from(ghPullRequest.getState()),
                toLocalDateTime(ghPullRequest.getCreatedAt()),
                toLocalDateTime(ghPullRequest.getUpdatedAt()),
                null
        );
    }

    private Pr(Long id, User author, PrStatus githubStatus,
              LocalDateTime githubCreatedAt, LocalDateTime githubUpdatedAt, String infos) {
        this.id = id;
        this.author = author;
        this.githubStatus = githubStatus;
        this.githubCreatedAt = githubCreatedAt;
        this.githubUpdatedAt = githubUpdatedAt;
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
        this.githubStatus = from(githubStatus);
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
}
