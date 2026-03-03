package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;

public class RepositoryGetRequest implements GithubRequest {

    private final String owner;
    private final String repository;

    public RepositoryGetRequest(String owner, String repository) {
        this.owner = owner;
        this.repository = repository;
    }

    @Override
    public String getUrl() {
        return String.format("/repos/%s/%s", owner, repository);
    }
}
