package com.devprmetrics.api.pr;

import com.devprmetrics.domain.pr.PrRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrApi {

    private final PrRepository prRepository;

    public PrApi(PrRepository prRepository) {
        this.prRepository = prRepository;
    }

    @Operation(summary = "Lista PRs com paginação e ordenação")
    @GetMapping("/api/prs")
    public Page<PrApiResponse> listAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return prRepository.findAll(pageable)
                .map(PrApiResponse::from);
    }

    @Operation(summary = "Busca PR por id")
    @GetMapping("/api/prs/{id}")
    public ResponseEntity<PrApiResponse> findById(@PathVariable Long id) {
        return prRepository.findById(id)
                .map(PrApiResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
