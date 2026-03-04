package com.devprmetrics.domain.repo;

//import com.devprmetrics.api.repository.RepositoryApiResponse;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

    private final GitHub gitHub;
    private final String organization;

    public RepoService(GitHub gitHub, @Value("${github.org}") String organization) {
        this.gitHub = gitHub;
        this.organization = organization;
    }

//    public void repositoryDetails(String name) throws IOException {
//        GHRepository repository = gitHub.getRepository(organization + "/" + name);
//
//        for (GHPullRequest pull : repository.queryPullRequests().state(GHIssueState.ALL).list()) {
//            if (pull.getCreatedAt() == null || pull.getCreatedAt().toInstant().isBefore(lastMonthCutoff)) {
//                continue;
//            }
//
//            List<RepositoryApiResponse.Review> reviews = new ArrayList<>();
//            for (GHPullRequestReview review : pull.listReviews()) {
//                reviews.add(new RepositoryApiResponse.Review(
//                        review.getId(),
//                        review.getUser() != null ? review.getUser().getId() : null,
//                        review.getUser() != null ? review.getUser().getLogin() : null,
//                        review.getState() != null ? review.getState().name() : null,
//                        review.getSubmittedAt() != null ? review.getSubmittedAt().toInstant() : null));
//            }
//
//            int additions = pull.getAdditions();
//            int deletions = pull.getDeletions();
//            int changedFiles = pull.getChangedFiles();
//            int openReviewComments = pull.getReviewComments();
//
//            pullsWithReviews.add(new RepositoryApiResponse.PullWithReviews(
//                    pull.getId(),
//                    pull.getNumber(),
//                    pull.getTitle(),
//                    pull.getHtmlUrl() != null ? pull.getHtmlUrl().toString() : null,
//                    pull.getState() != null ? pull.getState().name() : null,
//                    pull.getCreatedAt() != null ? pull.getCreatedAt().toInstant() : null,
//                    pull.getUpdatedAt() != null ? pull.getUpdatedAt().toInstant() : null,
//                    additions,
//                    deletions,
//                    additions + deletions,
//                    changedFiles,
//                    openReviewComments,
//                    openReviewComments > 0,
//                    reviews));
//        }
//
//        Map<String, Long> languages = repository.listLanguages();
//
//        return new RepositoryApiResponse(
//                repository.getId(),
//                repository.getName(),
//                repository.getFullName(),
//                repository.getHtmlUrl() != null ? repository.getHtmlUrl().toString() : null,
//                repository.isPrivate(),
//                repository.getDefaultBranch(),
//                organization,
//                languages,
//                pullsWithReviews);
//    }
}
