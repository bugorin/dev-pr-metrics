package com.devprmetrics.api;

//import com.devprmetrics.api.repository.RepositoryApiResponse;
import com.devprmetrics.domain.repo.RepoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryApi {

    private final RepoService repoService;

    public RepositoryApi(RepoService repoService) {
        this.repoService = repoService;
    }

//    @GetMapping("/api/repository/{name}")
//    public void repository(@PathVariable String name) throws IOException {
//        return repoService.repositoryDetails(name);
//    }
}
