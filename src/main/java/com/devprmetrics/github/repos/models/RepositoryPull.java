package com.devprmetrics.github.repos.models;

public record RepositoryPull(
        long number,
        String title,
        String state,
        String htmlUrl,
        String authorLogin) {
}
