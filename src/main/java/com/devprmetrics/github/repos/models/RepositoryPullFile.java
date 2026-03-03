package com.devprmetrics.github.repos.models;

public record RepositoryPullFile(
        String filename,
        String status,
        int additions,
        int deletions,
        int changes) {
}
