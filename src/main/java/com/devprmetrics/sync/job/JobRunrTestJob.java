package com.devprmetrics.sync.job;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobRunrTestJob {

    @Job(name = "Sync PR id=%0")
    public void execute(Long prId) {
        log.info("JobRunr test job executed at {} with payload={}", LocalDateTime.now(), prId);
    }
}
