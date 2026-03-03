package com.devprmetrics.github.repos.models;

public record RepositoryCommit(
        String sha,
        String message,
        String authorName,
        String authorDate) {
}
