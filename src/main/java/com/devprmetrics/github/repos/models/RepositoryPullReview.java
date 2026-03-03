package com.devprmetrics.github.repos.models;

public record RepositoryPullReview(
        long id,
        String state,
        String body,
        String authorLogin) {
}
