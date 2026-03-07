package com.devprmetrics.api.groupmember;

import com.devprmetrics.domain.group.Group;
import com.devprmetrics.domain.group.GroupRepository;
import com.devprmetrics.domain.teammember.GroupMember;
import com.devprmetrics.domain.teammember.GroupMemberRepository;
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

    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public TeamMemberApi(
            GroupMemberRepository groupMemberRepository,
            GroupRepository groupRepository,
            UserRepository userRepository) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Lista vínculos de membros com paginação")
    @GetMapping("/api/group-members")
    public Page<TeamMemberApiResponse> listAll(
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return groupMemberRepository.findAll(pageable).map(TeamMemberApiResponse::from);
    }

    @Operation(summary = "Lista membros por time com paginação")
    @GetMapping("/api/groups/{groupId}/members")
    public Page<TeamMemberApiResponse> listByGroup(
            @PathVariable Long groupId,
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        if (!groupRepository.existsById(groupId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "group not found");
        }

        return groupMemberRepository.findByGroupId(groupId, pageable).map(TeamMemberApiResponse::from);
    }

    @Operation(summary = "Cadastra vínculo de usuário em time")
    @PostMapping("/api/group-members")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamMemberApiResponse create(@Valid @RequestBody TeamMemberApiRequest request) {
        Group group = groupRepository.findById(request.groupId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "group not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        if (groupMemberRepository.existsByGroupIdAndUserId(group.getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "group member already exists");
        }

        GroupMember saved = groupMemberRepository.save(new GroupMember(group, user, request.role()));
        return TeamMemberApiResponse.from(saved);
    }
}
