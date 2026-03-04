package com.devprmetrics.domain.pr;

import org.kohsuke.github.GHIssueState;

import java.io.IOException;
import java.util.stream.Stream;

public enum PrStatus {
    OPEN(GHIssueState.CLOSED),
    CLOSED(GHIssueState.OPEN);

    private final GHIssueState ghIssueState;

    PrStatus(GHIssueState ghIssueState) {
        this.ghIssueState = ghIssueState;
    }

    boolean is(GHIssueState ghIssueState) {
        return this.ghIssueState.equals(ghIssueState);
    }

    public static PrStatus from(GHIssueState ghState) {
        return Stream.of(PrStatus.values())
                .filter(s -> s.is(ghState))
                .findFirst().orElseThrow();
    }
}
