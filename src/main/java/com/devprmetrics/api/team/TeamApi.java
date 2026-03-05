package com.devprmetrics.api.team;

import com.devprmetrics.domain.team.Team;
import com.devprmetrics.domain.team.TeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeamApi {

    private final TeamRepository teamRepository;

    public TeamApi(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Operation(summary = "Lista times com paginação")
    @GetMapping("/api/teams")
    public Page<TeamApiResponse> listTeams(
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return teamRepository.findAll(pageable).map(TeamApiResponse::from);
    }

    @Operation(summary = "Cadastra um time")
    @PostMapping("/api/teams")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamApiResponse createTeam(@Valid @RequestBody TeamApiRequest request) {
        String normalizedName = request.name().trim();
        if (teamRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "team name already exists");
        }

        Team saved = teamRepository.save(new Team(normalizedName));
        return TeamApiResponse.from(saved);
    }
}
