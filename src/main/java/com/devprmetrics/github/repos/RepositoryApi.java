package com.devprmetrics.github.repos;

import com.devprmetrics.github.repos.models.Repository;
import com.devprmetrics.github.repos.models.RepositoryActionRun;
import com.devprmetrics.github.repos.models.RepositoryBranch;
import com.devprmetrics.github.repos.models.RepositoryCommit;
import com.devprmetrics.github.repos.models.RepositoryContributor;
import com.devprmetrics.github.repos.models.RepositoryIssue;
import com.devprmetrics.github.repos.models.RepositoryPull;
import com.devprmetrics.github.repos.models.RepositoryPullFile;
import com.devprmetrics.github.repos.models.RepositoryPullReview;
import com.devprmetrics.github.repos.request.RepositoryPullsRequest;

import java.util.List;
import java.util.Map;

public interface RepositoryApi {

    List<Repository> listRepositories(String organization);

    Repository getRepository(String owner, String repository);

    List<RepositoryBranch> listBranches(String owner, String repository);

    List<RepositoryPull> listPullRequests(RepositoryPullsRequest request);

    RepositoryPull getPullRequest(String owner, String repository, long pullNumber);

    List<RepositoryPullReview> listPullRequestReviews(String owner, String repository, long pullNumber);

    List<RepositoryPullFile> listPullRequestFiles(String owner, String repository, long pullNumber);

    List<RepositoryCommit> listCommits(String owner, String repository);

    List<RepositoryContributor> listContributors(String owner, String repository);

    Map<String, Long> listLanguages(String owner, String repository);

    List<RepositoryActionRun> listActionRuns(String owner, String repository);

    List<RepositoryIssue> listIssues(String owner, String repository);
}
