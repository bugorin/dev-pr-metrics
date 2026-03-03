package com.devprmetrics.github.repos.request;

import com.devprmetrics.github.GithubRequest;
import java.util.StringJoiner;

public class RepositoryContributorsRequest implements GithubRequest {

    private final String owner;
    private final String repository;
    private final Long perPage;
    private final Long page;
    private final String anon;

    public RepositoryContributorsRequest(String owner, String repository, Long perPage, Long page, String anon) {
        this.owner = owner;
        this.repository = repository;
        this.perPage = perPage;
        this.page = page;
        this.anon = anon;
    }

    public RepositoryContributorsRequest(String owner, String repository) {
        this(owner, repository, 100L, 1L, null);
    }

    @Override
    public String getUrl() {
        String base = String.format("/repos/%s/%s/contributors", owner, repository);

        StringJoiner query = new StringJoiner("&");
        if (perPage != null) {
            query.add("per_page=" + perPage);
        }
        if (page != null) {
            query.add("page=" + page);
        }
        if (anon != null) {
            query.add("anon=" + anon);
        }
        return query.length() > 0 ? base + "?" + query : base;
    }
}
