package com.devprmetrics.github.repos.models;

public record RepositoryIssue(
        long number,
        String title,
        String state,
        String htmlUrl,
        String authorLogin,
        boolean pullRequest) {
}
