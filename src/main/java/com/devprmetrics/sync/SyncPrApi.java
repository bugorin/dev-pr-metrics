package com.devprmetrics.sync;

import com.devprmetrics.domain.user.User;
import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class SyncPrApi {

    private final SyncPrService syncPrService;

    public SyncPrApi(SyncPrService syncPrService) {
        this.syncPrService = syncPrService;
    }

    @GetMapping("/sync/pr/repository/{nome}")
    public List<User> syncByRepository(@PathVariable("nome") String nome) throws IOException {
        return syncPrService.syncReviewers(nome);
    }
}