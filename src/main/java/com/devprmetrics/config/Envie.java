package com.devprmetrics.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Envie {

    private final String organization;

    public Envie(@Value("${github.org}") String organization) {
        this.organization = organization;
    }

    public String getOrganization() {
        return organization;
    }
}
