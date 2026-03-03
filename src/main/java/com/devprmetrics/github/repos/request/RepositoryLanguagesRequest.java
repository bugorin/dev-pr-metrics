package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;

public class RepositoryLanguagesRequest implements GithubRequest {

    private final String owner;
    private final String repository;

    public RepositoryLanguagesRequest(String owner, String repository) {
        this.owner = owner;
        this.repository = repository;
    }

    @Override
    public String getUrl() {
        return String.format("/repos/%s/%s/languages", owner, repository);
    }
}
