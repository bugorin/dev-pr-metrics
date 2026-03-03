package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;
import java.util.StringJoiner;

public class RepositoryPullFilesRequest implements GithubRequest {

    private final String owner;
    private final String repository;
    private final long pullNumber;
    private final Long perPage;
    private final Long page;

    public RepositoryPullFilesRequest(String owner, String repository, long pullNumber, Long perPage, Long page) {
        this.owner = owner;
        this.repository = repository;
        this.pullNumber = pullNumber;
        this.perPage = perPage;
        this.page = page;
    }

    public RepositoryPullFilesRequest(String owner, String repository, long pullNumber) {
        this(owner, repository, pullNumber, 100L, 1L);
    }

    @Override
    public String getUrl() {
        String base = String.format("/repos/%s/%s/pulls/%d/files", owner, repository, pullNumber);

        StringJoiner query = new StringJoiner("&");
        if (perPage != null) {
            query.add("per_page=" + perPage);
        }
        if (page != null) {
            query.add("page=" + page);
        }
        return query.length() > 0 ? base + "?" + query : base;
    }
}
