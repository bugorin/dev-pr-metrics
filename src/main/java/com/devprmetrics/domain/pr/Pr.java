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

    @Column(name = "external_id", nullable = false, unique = true)
    private Long externalId;

    @Column(name = "number", nullable = false)
    private Integer number;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "repository_id", nullable = false)
    private Repo repository;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PrStatus status;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "draft", nullable = false)
    private boolean draft;

    @Column(name = "merged", nullable = false)
    private boolean merged;

    @Column(name = "merged_at")
    private LocalDateTime mergedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", name = "infos")
    private PrInfo info = new PrInfo();

    @JsonManagedReference
    @OneToMany(mappedBy = "pr", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    private List<Reviewer> reviewers = new ArrayList<>();

    protected Pr() {
    }

    public Pr(Long externalId, Integer number, User author, Repo repository, PrStatus status,
              String title, boolean draft, boolean merged,
              LocalDateTime mergedAt, LocalDateTime closedAt,
              String htmlUrl, int additions, int deletions,
              int fileChanges, int openReviewComments,
              int comments, int commits,
              String baseRef, String headRef,
              List<String> labels, List<Long> requestedReviewers,
              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.externalId = externalId;
        this.number = number;
        this.author = author;
        this.repository = repository;
        this.status = status;
        this.title = title;
        this.draft = draft;
        this.merged = merged;
        this.mergedAt = mergedAt;
        this.closedAt = closedAt;
        this.info = new PrInfo(
                htmlUrl,
                additions,
                deletions,
                fileChanges,
                openReviewComments,
                comments,
                commits,
                baseRef,
                headRef,
                labels,
                requestedReviewers
        );
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Pr merge(Pr pr) {
        this.setExternalId(pr.getExternalId());
        this.setNumber(pr.getNumber());
        this.setRepository(pr.getRepository());
        this.setStatus(pr.getStatus());
        this.setTitle(pr.getTitle());
        this.setDraft(pr.isDraft());
        this.setMerged(pr.isMerged());
        this.setMergedAt(pr.getMergedAt());
        this.setClosedAt(pr.getClosedAt());
        this.info = new PrInfo(
                pr.getHtmlUrl(),
                pr.getAdditions(),
                pr.getDeletions(),
                pr.getFileChanges(),
                pr.getOpenReviewComments(),
                pr.getComments(),
                pr.getCommits(),
                pr.getBaseRef(),
                pr.getHeadRef(),
                pr.getLabels(),
                pr.getRequestedReviewers()
        );
        this.setCreatedAt(pr.getCreatedAt());
        this.setUpdatedAt(pr.getUpdatedAt());

        for (Reviewer reviewer : pr.getReviewers()) {
            this.addOrUpdate(reviewer);
        }

        return this;
    }

    public int getOpenReviewComments() {
        return this.info.getOpenReviewComments();
    }

    public int getFileChanges() {
        return this.info.getFileChanges();
    }

    public int getDeletions() {
        return this.info.getDeletions();
    }

    public int getAdditions() {
        return this.info.getAdditions();
    }

    public String getHtmlUrl() {
        return this.info.getHtmlUrl();
    }

    public int getComments() {
        return this.info.getComments();
    }

    public int getCommits() {
        return this.info.getCommits();
    }

    public String getBaseRef() {
        return this.info.getBaseRef();
    }

    public String getHeadRef() {
        return this.info.getHeadRef();
    }

    public List<String> getLabels() {
        return this.info.getLabels();
    }

    public List<Long> getRequestedReviewers() {
        return this.info.getRequestedReviewers();
    }

    public void addOrUpdate(Reviewer reviewer) {
        Optional<Reviewer> existing = reviewers.stream()
                .filter(r -> r.isUser(reviewer.getUser()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().merge(reviewer);
            return;
        }

        reviewer.setPr(this);
        this.reviewers.add(reviewer);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class PrInfo implements Serializable {
        private String htmlUrl;
        private int additions;
        private int deletions;
        private int fileChanges;
        private int openReviewComments;
        private int comments;
        private int commits;
        private String baseRef;
        private String headRef;
        private List<String> labels = new ArrayList<>();
        private List<Long> requestedReviewers = new ArrayList<>();
    }
}
