package com.devprmetrics.github.repos.models;

public record RepositoryContributor(
        String login,
        long contributions,
        String htmlUrl) {
}
