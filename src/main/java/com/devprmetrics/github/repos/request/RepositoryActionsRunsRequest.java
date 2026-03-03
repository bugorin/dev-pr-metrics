package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;
import java.util.StringJoiner;

public class RepositoryActionsRunsRequest implements GithubRequest {

    private final String owner;
    private final String repository;
    private final Long perPage;
    private final Long page;
    private final String status;
    private final String branch;
    private final String event;

    public RepositoryActionsRunsRequest(
            String owner,
            String repository,
            Long perPage,
            Long page,
            String status,
            String branch,
            String event) {
        this.owner = owner;
        this.repository = repository;
        this.perPage = perPage;
        this.page = page;
        this.status = status;
        this.branch = branch;
        this.event = event;
    }

    public RepositoryActionsRunsRequest(String owner, String repository) {
        this(owner, repository, 30L, 1L, null, null, null);
    }

    @Override
    public String getUrl() {
        String base = String.format("/repos/%s/%s/actions/runs", owner, repository);

        StringJoiner query = new StringJoiner("&");
        if (perPage != null) {
            query.add("per_page=" + perPage);
        }
        if (page != null) {
            query.add("page=" + page);
        }
        if (status != null) {
            query.add("status=" + status);
        }
        if (branch != null) {
            query.add("branch=" + branch);
        }
        if (event != null) {
            query.add("event=" + event);
        }
        return query.length() > 0 ? base + "?" + query : base;
    }
}
