package com.devprmetrics.sync.service;

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

import java.util.*;

@Service
public class PrHandleService {

    private final PrRepository prRepository;
    private final UserCreateService userCreateService;

    public PrHandleService(PrRepository prRepository, UserCreateService userCreateService) {
        this.prRepository = prRepository;
        this.userCreateService = userCreateService;
    }

    @Transactional
    public void createOrMerge(Repo repo, GHPullRequest ghPullRequest) {
        Optional<Pr> byId = prRepository.findByExternalId(ghPullRequest.getId());

        if (byId.isEmpty()) {
            create(repo, ghPullRequest);
            return;
        }

        Pr pr = byId.get();
        pr.merge(PrEntityMapper.mapper(repo, ghPullRequest));
        saveUsers(pr);
        prRepository.save(pr);
    }

    private void create(Repo repository, GHPullRequest ghPullRequest) {
        Pr pr = PrEntityMapper.mapper(repository, ghPullRequest);
        saveUsers(pr);
        prRepository.save(pr);
    }

    private void saveUsers(Pr pr) {
        List<User> users = new ArrayList<>();
        users.add(pr.getAuthor());
        users.addAll(pr.getReviewers().stream().map(Reviewer::getUser).toList());
        userCreateService.findOrCreated(users);
    }
}
