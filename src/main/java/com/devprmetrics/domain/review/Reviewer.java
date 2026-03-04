package com.devprmetrics.domain.review;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.user.User;
import com.devprmetrics.util.LocalDateTimeUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHPullRequestReviewState;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import static com.devprmetrics.domain.review.ReviewerStatus.from;
import static com.devprmetrics.util.LocalDateTimeUtils.toLocalDateTime;

@Entity
public class Reviewer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_pr", nullable = false)
    private Pr pr;

    @ManyToOne(optional = false)
    @JoinColumn(name = "github_user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ReviewerStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    protected Reviewer() {
    }

    public Reviewer(Pr pr, User user, GHPullRequestReview reviewer) throws IOException {
        this(pr, user, from(reviewer.getState()), toLocalDateTime(reviewer.getSubmittedAt()));
    }

    public Reviewer(Pr pr, User user, ReviewerStatus status, LocalDateTime submittedAt) {
        this.pr = pr;
        this.user = user;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    public Long getId() {
        return id;
    }

    public Pr getPr() {
        return pr;
    }

    public void setPr(Pr pr) {
        this.pr = pr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReviewerStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewerStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void merge(GHPullRequestReview reviewer) throws IOException {
        this.status = from(reviewer.getState());
        this.submittedAt = toLocalDateTime(reviewer.getSubmittedAt());
    }

    public boolean isUser(User user) {
        return this.user.getId().equals(user.getId());
    }
}
