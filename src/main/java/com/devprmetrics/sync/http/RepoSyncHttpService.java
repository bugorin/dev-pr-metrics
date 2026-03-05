package com.devprmetrics.sync.http;

import com.devprmetrics.config.Envie;
import lombok.AllArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class RepoSyncHttpService {

    private final GitHub gitHub;
    private final Envie envie;

    public List<GHRepository> findAll() throws IOException {
        return gitHub.getOrganization(envie.getOrganization())
                .listRepositories().toList();
    }

    public GHRepository findBy(String repo) throws IOException {
        return gitHub.getOrganization(envie.getOrganization())
                .getRepository(repo);
    }
}
