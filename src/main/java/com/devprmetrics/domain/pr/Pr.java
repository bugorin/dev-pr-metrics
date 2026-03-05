package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
public class Pr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "github_id", nullable = false, unique = true)
    private Long githubId;

    @Column(name = "github_number", nullable = false)
    private Integer githubNumber;

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

    @Column(name = "github_draft", nullable = false)
    private boolean githubDraft;

    @Column(name = "github_merged", nullable = false)
    private boolean githubMerged;

    @Column(name = "github_merged_at")
    private LocalDateTime githubMergedAt;

    @Column(name = "github_closed_at")
    private LocalDateTime githubClosedAt;

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

    public Pr(Long githubId, Integer githubNumber, User author, Repo repository, PrStatus githubStatus,
              String githubTitle, boolean githubDraft, boolean githubMerged,
              LocalDateTime githubMergedAt, LocalDateTime githubClosedAt,
              String githubHtmlUrl, int githubAdditions, int githubDeletions,
              int githubFileChanges, int githubOpenReviewComments,
              int githubComments, int githubCommits,
              String githubBaseRef, String githubHeadRef,
              List<String> githubLabels, List<Long> githubRequestedReviewers,
              LocalDateTime githubCreatedAt, LocalDateTime githubUpdatedAt) {
        this.githubId = githubId;
        this.githubNumber = githubNumber;
        this.author = author;
        this.repository = repository;
        this.githubStatus = githubStatus;
        this.githubTitle = githubTitle;
        this.githubDraft = githubDraft;
        this.githubMerged = githubMerged;
        this.githubMergedAt = githubMergedAt;
        this.githubClosedAt = githubClosedAt;
        this.info = new PrInfo(
                githubHtmlUrl,
                githubAdditions,
                githubDeletions,
                githubFileChanges,
                githubOpenReviewComments,
                githubComments,
                githubCommits,
                githubBaseRef,
                githubHeadRef,
                githubLabels,
                githubRequestedReviewers
        );
        this.githubCreatedAt = githubCreatedAt;
        this.githubUpdatedAt = githubUpdatedAt;
    }

    public Pr merge(Pr pr) {
        this.setGithubId(pr.getGithubId());
        this.setGithubNumber(pr.getGithubNumber());
        this.setRepository(pr.getRepository());
        this.setGithubStatus(pr.getGithubStatus());
        this.setGithubTitle(pr.getGithubTitle());
        this.setGithubDraft(pr.isGithubDraft());
        this.setGithubMerged(pr.isGithubMerged());
        this.setGithubMergedAt(pr.getGithubMergedAt());
        this.setGithubClosedAt(pr.getGithubClosedAt());
        this.info = new PrInfo(
                pr.getGithubHtmlUrl(),
                pr.getGithubAdditions(),
                pr.getGithubDeletions(),
                pr.getGithubFileChanges(),
                pr.getGithubOpenReviewComments(),
                pr.getGithubComments(),
                pr.getGithubCommits(),
                pr.getGithubBaseRef(),
                pr.getGithubHeadRef(),
                pr.getGithubLabels(),
                pr.getGithubRequestedReviewers()
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

    public int getGithubComments() {
        return this.info.getGithubComments();
    }

    public int getGithubCommits() {
        return this.info.getGithubCommits();
    }

    public String getGithubBaseRef() {
        return this.info.getGithubBaseRef();
    }

    public String getGithubHeadRef() {
        return this.info.getGithubHeadRef();
    }

    public List<String> getGithubLabels() {
        return this.info.getGithubLabels();
    }

    public List<Long> getGithubRequestedReviewers() {
        return this.info.getGithubRequestedReviewers();
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
        private int githubComments;
        private int githubCommits;
        private String githubBaseRef;
        private String githubHeadRef;
        private List<String> githubLabels = new ArrayList<>();
        private List<Long> githubRequestedReviewers = new ArrayList<>();
    }
}
