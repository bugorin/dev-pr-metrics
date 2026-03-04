package com.devprmetrics.sync;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.repo.RepoRepository;
import com.devprmetrics.sync.mapper.RepoEntityMapper;
import java.util.Optional;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepoHandleService {

    private final RepoRepository repoRepository;

    public RepoHandleService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    @Transactional
    public Repo createOrMerge(GHRepository ghRepository) {
        Optional<Repo> existing = repoRepository.findById(ghRepository.getId());
        if (existing.isEmpty()) {
            return create(ghRepository);
        }

        Repo merged = existing.get().merge(RepoEntityMapper.mapper(ghRepository));
        return repoRepository.save(merged);
    }

    @Transactional
    public Repo create(GHRepository ghRepository) {
        return repoRepository.save(RepoEntityMapper.mapper(ghRepository));
    }
}
