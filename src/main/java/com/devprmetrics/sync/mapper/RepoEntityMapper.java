package com.devprmetrics.sync.mapper;

import com.devprmetrics.domain.repo.Repo;
import org.kohsuke.github.GHRepository;

public record RepoEntityMapper() {

    public static Repo mapper(GHRepository ghRepository) {
        try {
            if (ghRepository == null) {
                throw new IllegalArgumentException("GHRepository is null");
            }

            String name = ghRepository.getName();
            String fullName = ghRepository.getFullName();
            String defaultBranch = ghRepository.getDefaultBranch();

            if (name == null || name.isBlank()) {
                name = "unknown-repository-" + ghRepository.getId();
            }
            if (fullName == null || fullName.isBlank()) {
                fullName = name;
            }
            if (defaultBranch == null || defaultBranch.isBlank()) {
                defaultBranch = "main";
            }

            return new Repo(
                    ghRepository.getId(),
                    name,
                    fullName,
                    ghRepository.getHtmlUrl() != null ? ghRepository.getHtmlUrl().toString() : null,
                    ghRepository.isPrivate(),
                    defaultBranch);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not map GHRepository to Repo entity", e);
        }
    }
}
