package com.devprmetrics.sync.service;

import com.devprmetrics.domain.repo.Repo;
import com.devprmetrics.domain.repo.RepoRepository;
import com.devprmetrics.sync.mapper.RepoEntityMapper;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RepoHandleService {

    private final RepoRepository repoRepository;

    @Transactional
    public void createOrMerge(GHRepository ghRepository) {
        Optional<Repo> existing = repoRepository.findById(ghRepository.getId());
        if (existing.isEmpty()) {
            create(ghRepository);
            return;
        }

        Repo merged = existing.get().merge(RepoEntityMapper.mapper(ghRepository));
        repoRepository.save(merged);
    }

    @Transactional
    public Repo findOrCreate(GHRepository ghRepository) {
        Optional<Repo> existing = repoRepository.findById(ghRepository.getId());
        return existing.orElseGet(() -> create(ghRepository));
    }

    private Repo create(GHRepository ghRepository) {
        return repoRepository.save(RepoEntityMapper.mapper(ghRepository));
    }
}
