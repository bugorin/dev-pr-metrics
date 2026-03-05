package com.devprmetrics.domain.review;

import com.devprmetrics.domain.pr.Pr;
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
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", name = "infos")
    private ReviewInfo info = new ReviewInfo();

    protected Reviewer() {
    }

    public Reviewer(Pr pr, User user, ReviewerStatus status, LocalDateTime submittedAt, String body) {
        this.pr = pr;
        this.user = user;
        this.status = status;
        this.submittedAt = submittedAt;
        this.info = new ReviewInfo(body);
    }

    public void merge(Reviewer reviewer) {
        this.status = reviewer.getStatus();
        this.submittedAt = reviewer.getSubmittedAt();
        this.info = new ReviewInfo(reviewer.getBody());
    }

    public String getBody() {
        return info.getBody();
    }

    public boolean isUser(User user) {
        return this.user.getId().equals(user.getId());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReviewInfo implements Serializable {
        private String body;
    }
}
