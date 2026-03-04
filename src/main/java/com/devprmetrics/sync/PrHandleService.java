package com.devprmetrics.sync;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrRepository;
import com.devprmetrics.domain.review.Reviewer;
import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserCreateService;
import com.devprmetrics.sync.mapper.PrEntityMapper;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    public Pr createOrMerge(GHPullRequest ghPullRequest) throws IOException {
        Optional<Pr> byId = prRepository.findById(ghPullRequest.getNumber());

        if (byId.isEmpty()) {
            return create(ghPullRequest);
        }

        Pr pr = byId.get();
        pr.merge(PrEntityMapper.mapper(ghPullRequest));
        prRepository.save(pr);
        return pr;
    }

    public Pr create(GHPullRequest ghPullRequest) throws IOException {
        Pr pr = PrEntityMapper.mapper(ghPullRequest);
        userCreateService.findOrCreated(pr.getAuthor());
        List<User> reviewrs = pr.getReviewers().stream().map(Reviewer::getUser).toList();
        userCreateService.findOrCreated(reviewrs);
        return prRepository.save(pr);
    }

}
