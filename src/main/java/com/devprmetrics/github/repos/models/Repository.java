package com.devprmetrics.github.repos.models;

public record Repository(String name, String htmlUrl, boolean isPrivate, String defaultBranch) {
}
