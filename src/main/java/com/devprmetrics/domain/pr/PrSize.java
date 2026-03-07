package com.devprmetrics.domain.pr;

public enum PrSize {
    PP(0, 50),
    P(51, 200),
    M(201, 500),
    G(501, 1000),
    GG(1001, Integer.MAX_VALUE);

    private final int min;
    private final int max;

    PrSize(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static PrSize from(int additions, int deletions) {
        int total = additions + deletions;

        for (PrSize size : values()) {
            if (total >= size.min && total <= size.max) {
                return size;
            }
        }

        throw new IllegalArgumentException("Invalid PR size: " + total);
    }
}
