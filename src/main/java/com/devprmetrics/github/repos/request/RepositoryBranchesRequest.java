package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;
import java.util.StringJoiner;

public class RepositoryBranchesRequest implements GithubRequest {

    private final String owner;
    private final String repository;
    private final Long perPage;
    private final Long page;
    private final Boolean onlyProtected;

    public RepositoryBranchesRequest(String owner, String repository, Long perPage, Long page, Boolean onlyProtected) {
        this.owner = owner;
        this.repository = repository;
        this.perPage = perPage;
        this.page = page;
        this.onlyProtected = onlyProtected;
    }

    public RepositoryBranchesRequest(String owner, String repository) {
        this(owner, repository, 100L, 1L, null);
    }

    @Override
    public String getUrl() {
        String base = String.format("/repos/%s/%s/branches", owner, repository);

        StringJoiner query = new StringJoiner("&");
        if (perPage != null) {
            query.add("per_page=" + perPage);
        }
        if (page != null) {
            query.add("page=" + page);
        }
        if (onlyProtected != null) {
            query.add("protected=" + onlyProtected);
        }
        return query.length() > 0 ? base + "?" + query : base;
    }
}
