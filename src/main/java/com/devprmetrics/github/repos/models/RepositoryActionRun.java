package com.devprmetrics.github.repos.models;

public record RepositoryActionRun(
        long id,
        String name,
        String status,
        String conclusion,
        String htmlUrl,
        String createdAt) {
}
