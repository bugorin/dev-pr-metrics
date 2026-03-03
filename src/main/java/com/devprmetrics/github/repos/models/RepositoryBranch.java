package com.devprmetrics.github.repos.models;

public record RepositoryBranch(String name, boolean isProtected, String commitSha) {
}
