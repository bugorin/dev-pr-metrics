package com.devprmetrics.domain.user;

import com.devprmetrics.sync.mapper.UserEntityMapper;
import org.kohsuke.github.GHUser;
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
    public Optional<User> findOrCreated(GHUser ghUser) {
        if (ghUser == null) throw new IllegalArgumentException("GHUser is null");
        return this.findOrCreated(List.of(ghUser)).stream().findFirst();
    }

    @Transactional
    public List<User> findOrCreated(List<GHUser> ghUsers) {
        if (ghUsers == null || ghUsers.isEmpty()) return List.of();

        Set<Long> githubIds = ghUsers.stream()
                .map(GHUser::getId)
                .collect(toSet());

        Set<Long> alreadySaved =
                userRepository.findAllById(githubIds)
                        .stream()
                        .map(User::getId)
                        .collect(toSet());

        Set<User> usersToCreate =
                ghUsers.stream()
                        .map(UserEntityMapper::mapper)
                        .filter(user -> !alreadySaved.contains(user.getId()))
                        .collect(toSet());

        if (!usersToCreate.isEmpty()) {
            userRepository.saveAll(usersToCreate);
        }

        return userRepository.findAllById(githubIds);
    }
}
