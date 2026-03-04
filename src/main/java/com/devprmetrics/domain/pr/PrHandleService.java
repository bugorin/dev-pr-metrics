package com.devprmetrics.domain.pr;

import com.devprmetrics.domain.user.UserCreateService;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

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
            return prRepository.save(Pr.createFrom(ghPullRequest, userCreateService));
        }

        Pr pr = byId.get();
        prRepository.save(pr.merge(ghPullRequest));
        return pr;
    }

}
