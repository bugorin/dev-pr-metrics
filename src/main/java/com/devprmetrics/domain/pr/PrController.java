package com.devprmetrics.domain.pr;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prs")
public class PrController {

    private final PrRepository prRepository;

    public PrController(PrRepository prRepository) {
        this.prRepository = prRepository;
    }

    @GetMapping
    public List<Pr> listAll() {
        return prRepository.findAll();
    }
}
