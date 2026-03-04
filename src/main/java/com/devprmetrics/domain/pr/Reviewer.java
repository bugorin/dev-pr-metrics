package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.User;
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
import java.time.LocalDateTime;

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
}
