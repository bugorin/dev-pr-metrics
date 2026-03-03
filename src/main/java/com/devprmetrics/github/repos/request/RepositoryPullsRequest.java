package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;
import java.util.StringJoiner;

public class RepositoryPullsRequest implements GithubRequest {

    public enum State {
        OPEN("open"),
        CLOSED("closed"),
        ALL("all");

        private final String paramName;

        State(String paramName) {
            this.paramName = paramName;
        }
    }

    private final String owner;
    private final String repository;
    private final State state;
    private final String sort;
    private final String direction;
    private final Long perPage;
    private final Long page;

    public RepositoryPullsRequest(
            String owner,
            String repository,
            State state,
            String sort,
            String direction,
            Long perPage,
            Long page) {
        this.owner = owner;
        this.repository = repository;
        this.state = state;
        this.sort = sort;
        this.direction = direction;
        this.perPage = perPage;
        this.page = page;
    }

    public RepositoryPullsRequest(String owner, String repository) {
        this(owner, repository, State.ALL, "created", "desc", 30L, 1L);
    }

    @Override
    public String getUrl() {
        String base = String.format("/repos/%s/%s/pulls", owner, repository);

        StringJoiner query = new StringJoiner("&");
        if (state != null) {
            query.add("state=" + state.paramName);
        }
        if (sort != null) {
            query.add("sort=" + sort);
        }
        if (direction != null) {
            query.add("direction=" + direction);
        }
        if (perPage != null) {
            query.add("per_page=" + perPage);
        }
        if (page != null) {
            query.add("page=" + page);
        }
        return query.length() > 0 ? base + "?" + query : base;
    }
}
