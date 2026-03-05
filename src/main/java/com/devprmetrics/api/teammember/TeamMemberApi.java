package com.devprmetrics.api.teammember;

import com.devprmetrics.domain.team.Team;
import com.devprmetrics.domain.team.TeamRepository;
import com.devprmetrics.domain.teammember.TeamMember;
import com.devprmetrics.domain.teammember.TeamMemberRepository;
import com.devprmetrics.domain.user.User;
import com.devprmetrics.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TeamMemberApi {

    private final TeamMemberRepository teamMemberRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamMemberApi(
            TeamMemberRepository teamMemberRepository,
            TeamRepository teamRepository,
            UserRepository userRepository) {
        this.teamMemberRepository = teamMemberRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Lista vínculos de membros com paginação")
    @GetMapping("/api/team-members")
    public Page<TeamMemberApiResponse> listAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return teamMemberRepository.findAll(pageable).map(TeamMemberApiResponse::from);
    }

    @Operation(summary = "Lista membros por time com paginação")
    @GetMapping("/api/teams/{teamId}/members")
    public Page<TeamMemberApiResponse> listByTeam(
            @PathVariable Long teamId,
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        if (!teamRepository.existsById(teamId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "team not found");
        }

        return teamMemberRepository.findByTeamId(teamId, pageable).map(TeamMemberApiResponse::from);
    }

    @Operation(summary = "Cadastra vínculo de usuário em time")
    @PostMapping("/api/team-members")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamMemberApiResponse create(@Valid @RequestBody TeamMemberApiRequest request) {
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "team not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (teamMemberRepository.existsByTeamIdAndUserId(team.getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "team member already exists");
        }

        TeamMember saved = teamMemberRepository.save(new TeamMember(team, user, request.role()));
        return TeamMemberApiResponse.from(saved);
    }
}
