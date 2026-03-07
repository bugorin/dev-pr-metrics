package com.devprmetrics.api.group;

import com.devprmetrics.domain.group.Group;
import com.devprmetrics.domain.group.GroupRepository;
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
public class GroupApi {

    private final GroupRepository groupRepository;

    public GroupApi(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Operation(summary = "Lista grupos com paginação")
    @GetMapping("/api/groups")
    public Page<GroupApiResponse> listGroups(
            @ParameterObject
            @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return groupRepository.findAll(pageable).map(GroupApiResponse::from);
    }

    @Operation(summary = "Cadastra um grupo")
    @PostMapping("/api/groups")
    @ResponseStatus(HttpStatus.CREATED)
    public GroupApiResponse createGroup(@Valid @RequestBody GroupApiRequest request) {
        String normalizedName = request.name().trim();
        if (groupRepository.findByNameIgnoreCase(normalizedName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "group name already exists");
        }

        Group saved = groupRepository.save(new Group(normalizedName));
        return GroupApiResponse.from(saved);
    }
}
