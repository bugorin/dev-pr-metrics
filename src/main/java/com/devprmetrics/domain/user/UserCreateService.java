package com.devprmetrics.domain.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class UserCreateService {

    private final UserRepository userRepository;

    public UserCreateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Optional<User> findOrCreated(User user) {
        return this.findOrCreated(List.of(user)).stream().findFirst();
    }

    @Transactional
    public List<User> findOrCreated(List<User> users) {
        if (users == null || users.isEmpty()) return List.of();

        Set<Long> githubIds = users.stream()
                .map(User::getId)
                .collect(toSet());

        Set<Long> alreadySaved =
                userRepository.findAllById(githubIds)
                        .stream()
                        .map(User::getId)
                        .collect(toSet());

        Set<User> usersToCreate =
                users.stream()
                        .filter(user -> !alreadySaved.contains(user.getId()))
                        .collect(toSet());

        if (!usersToCreate.isEmpty()) {
            userRepository.saveAll(usersToCreate);
        }

        return userRepository.findAllById(githubIds);
    }
}
