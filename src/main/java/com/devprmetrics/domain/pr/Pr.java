package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.devprmetrics.util.LocalDateTimeUtils.toLocalDateTime;

@Entity
@Data
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

    protected Pr() {}

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
        Optional<Reviewer> existing = reviewers.stream()
                .filter(r -> r.isUser(user))
                .findFirst();

        if(existing.isPresent()) {
            existing.get().merge(reviewer);
            return;
        }

        this.reviewers.add(new Reviewer(this, user, reviewer));
    }

    public void setGithubStatus(GHIssueState githubStatus) {
        this.githubStatus = PrStatus.from(githubStatus);
    }

    public void setGithubCreatedAt(LocalDateTime githubCreatedAt) {
        this.githubCreatedAt = githubCreatedAt;
    }

    public void setGithubCreatedAt(Date githubCreatedAt) {
        this.setGithubCreatedAt(toLocalDateTime(githubCreatedAt));
    }

    public void setGithubUpdatedAt(LocalDateTime githubUpdatedAt) {
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public void setGithubUpdatedAt(Date githubUpdatedAt) {
        this.setGithubUpdatedAt(toLocalDateTime(githubUpdatedAt));
    }
}
