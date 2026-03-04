package com.devprmetrics.sync;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrRepository;
import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserCreateService;
import com.devprmetrics.sync.mapper.PrEntityMapper;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrHandleService {

    private final PrRepository prRepository;
    private final UserCreateService userCreateService;

    public PrHandleService(PrRepository prRepository, UserCreateService userCreateService) {
        this.prRepository = prRepository;
        this.userCreateService = userCreateService;
    }

    @Transactional
    public Pr createOrMerge(Repo repo, GHPullRequest ghPullRequest) {
        Optional<Pr> byId = prRepository.findById((long) ghPullRequest.getNumber());

        if (byId.isEmpty()) {
            return create(repo, ghPullRequest);
        }

        Pr pr = byId.get();
        pr.merge(PrEntityMapper.mapper(repo, ghPullRequest));
        prRepository.save(pr);
        return pr;
    }

    public Pr create(Repo repository, GHPullRequest ghPullRequest) {
        Pr pr = PrEntityMapper.mapper(repository, ghPullRequest);
        userCreateService.findOrCreated(pr.getAuthor());
        List<User> reviewrs = pr.getReviewers().stream().map(Reviewer::getUser).toList();
        userCreateService.findOrCreated(reviewrs);
        return prRepository.save(pr);
    }
}
