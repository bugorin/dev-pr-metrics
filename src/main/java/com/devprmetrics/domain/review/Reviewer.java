package com.devprmetrics.domain.review;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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

    public void merge(Reviewer reviewer) {
        this.status = reviewer.getStatus();
        this.submittedAt = reviewer.getSubmittedAt();
    }

    public boolean isUser(User user) {
        return this.user.getId().equals(user.getId());
    }
}
