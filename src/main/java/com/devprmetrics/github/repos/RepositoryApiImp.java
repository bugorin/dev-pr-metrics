package com.devprmetrics.github.repos;

import com.devprmetrics.github.GitHubHttpClient;
import com.devprmetrics.github.repos.models.Repository;
import com.devprmetrics.github.repos.models.RepositoryActionRun;
import com.devprmetrics.github.repos.models.RepositoryBranch;
import com.devprmetrics.github.repos.models.RepositoryCommit;
import com.devprmetrics.github.repos.models.RepositoryContributor;
import com.devprmetrics.github.repos.models.RepositoryIssue;
import com.devprmetrics.github.repos.models.RepositoryPull;
import com.devprmetrics.github.repos.models.RepositoryPullFile;
import com.devprmetrics.github.repos.models.RepositoryPullReview;
import com.devprmetrics.github.repos.request.RepositoryActionsRunsRequest;
import com.devprmetrics.github.repos.request.RepositoryBranchesRequest;
import com.devprmetrics.github.repos.request.RepositoryCommitsRequest;
import com.devprmetrics.github.repos.request.RepositoryContributorsRequest;
import com.devprmetrics.github.repos.request.RepositoryGetRequest;
import com.devprmetrics.github.repos.request.RepositoryIssuesRequest;
import com.devprmetrics.github.repos.request.RepositoryLanguagesRequest;
import com.devprmetrics.github.repos.request.RepositoryListRequest;
import com.devprmetrics.github.repos.request.RepositoryPullDetailRequest;
import com.devprmetrics.github.repos.request.RepositoryPullFilesRequest;
import com.devprmetrics.github.repos.request.RepositoryPullReviewsRequest;
import com.devprmetrics.github.repos.request.RepositoryPullsRequest;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RepositoryApiImp implements RepositoryApi {

    private final GitHubHttpClient gitHubHttpClient;

    public RepositoryApiImp(GitHubHttpClient gitHubHttpClient) {
        this.gitHubHttpClient = gitHubHttpClient;
    }

    @Override
    public List<Repository> listRepositories(String organization) {
        JsonNode response = gitHubHttpClient.get(new RepositoryListRequest(organization));
        List<Repository> repositories = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return repositories;
        }

        for (JsonNode node : response) {
            repositories.add(mapRepository(node));
        }
        return repositories;
    }

    @Override
    public Repository getRepository(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryGetRequest(owner, repository));
        if (response == null || !response.isObject()) {
            return new Repository("", "#", false, "main");
        }
        return mapRepository(response);
    }

    @Override
    public List<RepositoryBranch> listBranches(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryBranchesRequest(owner, repository));
        List<RepositoryBranch> branches = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return branches;
        }

        for (JsonNode node : response) {
            branches.add(new RepositoryBranch(
                    node.path("name").asText(""),
                    node.path("protected").asBoolean(false),
                    node.path("commit").path("sha").asText("")));
        }
        return branches;
    }

    @Override
    public List<RepositoryPull> listPullRequests(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryPullsRequest(owner, repository));
        List<RepositoryPull> pulls = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return pulls;
        }

        for (JsonNode node : response) {
            pulls.add(mapPull(node));
        }
        return pulls;
    }

    @Override
    public RepositoryPull getPullRequest(String owner, String repository, long pullNumber) {
        JsonNode response = gitHubHttpClient.get(new RepositoryPullDetailRequest(owner, repository, pullNumber));
        if (response == null || !response.isObject()) {
            return new RepositoryPull(0L, "", "", "#", "");
        }
        return mapPull(response);
    }

    @Override
    public List<RepositoryPullReview> listPullRequestReviews(String owner, String repository, long pullNumber) {
        JsonNode response = gitHubHttpClient.get(new RepositoryPullReviewsRequest(owner, repository, pullNumber));
        List<RepositoryPullReview> reviews = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return reviews;
        }

        for (JsonNode node : response) {
            reviews.add(new RepositoryPullReview(
                    node.path("id").asLong(0L),
                    node.path("state").asText(""),
                    node.path("body").asText(""),
                    node.path("user").path("login").asText("")));
        }
        return reviews;
    }

    @Override
    public List<RepositoryPullFile> listPullRequestFiles(String owner, String repository, long pullNumber) {
        JsonNode response = gitHubHttpClient.get(new RepositoryPullFilesRequest(owner, repository, pullNumber));
        List<RepositoryPullFile> files = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return files;
        }

        for (JsonNode node : response) {
            files.add(new RepositoryPullFile(
                    node.path("filename").asText(""),
                    node.path("status").asText(""),
                    node.path("additions").asInt(0),
                    node.path("deletions").asInt(0),
                    node.path("changes").asInt(0)));
        }
        return files;
    }

    @Override
    public List<RepositoryCommit> listCommits(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryCommitsRequest(owner, repository));
        List<RepositoryCommit> commits = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return commits;
        }

        for (JsonNode node : response) {
            commits.add(new RepositoryCommit(
                    node.path("sha").asText(""),
                    node.path("commit").path("message").asText(""),
                    node.path("commit").path("author").path("name").asText(""),
                    node.path("commit").path("author").path("date").asText("")));
        }
        return commits;
    }

    @Override
    public List<RepositoryContributor> listContributors(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryContributorsRequest(owner, repository));
        List<RepositoryContributor> contributors = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return contributors;
        }

        for (JsonNode node : response) {
            contributors.add(new RepositoryContributor(
                    node.path("login").asText(""),
                    node.path("contributions").asLong(0L),
                    node.path("html_url").asText("#")));
        }
        return contributors;
    }

    @Override
    public Map<String, Long> listLanguages(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryLanguagesRequest(owner, repository));
        Map<String, Long> languages = new HashMap<>();
        if (response == null || !response.isObject()) {
            return languages;
        }

        response.fields().forEachRemaining(entry -> languages.put(entry.getKey(), entry.getValue().asLong(0L)));
        return languages;
    }

    @Override
    public List<RepositoryActionRun> listActionRuns(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryActionsRunsRequest(owner, repository));
        List<RepositoryActionRun> runs = new ArrayList<>();
        if (response == null || !response.isObject()) {
            return runs;
        }

        JsonNode workflowRuns = response.path("workflow_runs");
        if (!workflowRuns.isArray()) {
            return runs;
        }

        for (JsonNode node : workflowRuns) {
            runs.add(new RepositoryActionRun(
                    node.path("id").asLong(0L),
                    node.path("name").asText(""),
                    node.path("status").asText(""),
                    node.path("conclusion").asText(""),
                    node.path("html_url").asText("#"),
                    node.path("created_at").asText("")));
        }
        return runs;
    }

    @Override
    public List<RepositoryIssue> listIssues(String owner, String repository) {
        JsonNode response = gitHubHttpClient.get(new RepositoryIssuesRequest(owner, repository));
        List<RepositoryIssue> issues = new ArrayList<>();
        if (response == null || !response.isArray()) {
            return issues;
        }

        for (JsonNode node : response) {
            issues.add(new RepositoryIssue(
                    node.path("number").asLong(0L),
                    node.path("title").asText(""),
                    node.path("state").asText(""),
                    node.path("html_url").asText("#"),
                    node.path("user").path("login").asText(""),
                    !node.path("pull_request").isMissingNode() && !node.path("pull_request").isNull()));
        }
        return issues;
    }

    private Repository mapRepository(JsonNode node) {
        return new Repository(
                node.path("name").asText(""),
                node.path("html_url").asText("#"),
                node.path("private").asBoolean(false),
                node.path("default_branch").asText("main"));
    }

    private RepositoryPull mapPull(JsonNode node) {
        return new RepositoryPull(
                node.path("number").asLong(0L),
                node.path("title").asText(""),
                node.path("state").asText(""),
                node.path("html_url").asText("#"),
                node.path("user").path("login").asText(""));
    }

}
