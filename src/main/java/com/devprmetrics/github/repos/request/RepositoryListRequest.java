package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;

public class RepositoryListRequest implements GithubRequest {

    private final String organization;
    private final Long per_page;
    private final String sort;

    public RepositoryListRequest(String organization, Long perPage, String sort) {
        this.organization = organization;
        per_page = perPage;
        this.sort = sort;
    }

    public RepositoryListRequest(String organization) {
        this.organization = organization;
        this.per_page = 100L;
        this.sort = "full_name";
    }

    @Override
    public String getUrl() {
        return String.format("/orgs/%s/repos?per_page=%d&sort=%s", organization, per_page, sort);
    }
}
