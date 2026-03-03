package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;

public class RepositoryPullDetailRequest implements GithubRequest {

    private final String owner;
    private final String repository;
    private final long pullNumber;

    public RepositoryPullDetailRequest(String owner, String repository, long pullNumber) {
        this.owner = owner;
        this.repository = repository;
        this.pullNumber = pullNumber;
    }

    @Override
    public String getUrl() {
        return String.format("/repos/%s/%s/pulls/%d", owner, repository, pullNumber);
    }
}
