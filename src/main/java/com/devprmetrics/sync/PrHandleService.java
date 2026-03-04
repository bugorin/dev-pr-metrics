package com.devprmetrics.sync;

import com.devprmetrics.domain.pr.Pr;
import com.devprmetrics.domain.pr.PrRepository;
import com.devprmetrics.sync.mapper.PrEntityMapper;
import org.kohsuke.github.GHPullRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrHandleService {

    private final PrRepository prRepository;

    public PrHandleService(PrRepository prRepository) {
        this.prRepository = prRepository;
    }

    public Pr createOrMerge(GHPullRequest ghPullRequest) {
        Optional<Pr> byId = prRepository.findById(ghPullRequest.getNumber());

        if (byId.isEmpty()) {
            return create(ghPullRequest);
        }

        Pr pr = byId.get();
        pr.merge(PrEntityMapper.mapper(ghPullRequest));
        prRepository.save(pr);
        return pr;
    }

    public Pr create(GHPullRequest ghPullRequest) {
        Pr pr = PrEntityMapper.mapper(ghPullRequest);
        return prRepository.save(pr);
    }

}
