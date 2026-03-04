package com.devprmetrics.api.user;

import com.devprmetrics.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    private final UserRepository userRepository;

    public UserApi(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Lista usuários com paginação e ordenação")
    @GetMapping("/api/users")
    public Page<UserApiResponse> listUsers(
            @ParameterObject @PageableDefault(page = 0, size = 20, sort = "id") Pageable pageable) {
        return userRepository.findAll(pageable).map(UserApiResponse::from);
    }
}
