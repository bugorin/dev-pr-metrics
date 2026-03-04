package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserCreateService;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHPullRequestReview;
import org.kohsuke.github.GHUser;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PrHandleService {

    private final PrRepository prRepository;
    private final UserCreateService userCreateService;

    public PrHandleService(PrRepository prRepository, UserCreateService userCreateService) {
        this.prRepository = prRepository;
        this.userCreateService = userCreateService;
    }

    public Pr createOrMerge(GHPullRequest ghPullRequest) throws IOException {
        Optional<Pr> byId = prRepository.findById(ghPullRequest.getNumber());

        if (byId.isEmpty()) {
            return create(ghPullRequest, userCreateService);
        }

        Pr pr = byId.get();

        Map<User, GHPullRequestReview> reviewByUser = mapToUser(ghPullRequest.listReviews().toList());
        pr.merge(ghPullRequest, reviewByUser);
        prRepository.save(pr);
        return pr;
    }

    public Pr create(GHPullRequest ghPullRequest, UserCreateService userCreateService) throws IOException {
        User author = userCreateService.findOrCreated(ghPullRequest.getUser())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<GHPullRequestReview> ghReviews = ghPullRequest.listReviews().toList();

        Pr pr = new Pr(
                ghPullRequest.getNumber(),
                author,
                ghPullRequest.getState(),
                ghPullRequest.getCreatedAt(),
                ghPullRequest.getUpdatedAt(),
                null
        );

        Map<User, GHPullRequestReview> reviewByUser = mapToUser(ghReviews);
        for (Map.Entry<User, GHPullRequestReview> key : reviewByUser.entrySet()) {
            pr.addOrUpdate(key.getKey(), key.getValue());
        }

        return prRepository.save(pr);
    }

    private Map<User, GHPullRequestReview> mapToUser(List<GHPullRequestReview> ghReviews) throws IOException {
        Map<Long, User> usersById = mapToUserAndGroupById(ghReviews);
        Map<User, GHPullRequestReview> result = new LinkedHashMap<>();
        for (GHPullRequestReview review : ghReviews) {
            GHUser ghUser = review.getUser();
            User user = usersById.get(ghUser.getId());
            result.put(user, review);
        }
        return result;
    }

    private Map<Long, User> mapToUserAndGroupById(List<GHPullRequestReview> ghReviews) throws IOException {
        List<GHUser> ghUsers = new ArrayList<>();
        for (GHPullRequestReview review : ghReviews) {
            ghUsers.add(review.getUser());
        }
        List<User> users = userCreateService.findOrCreated(ghUsers);
        return users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }


}
