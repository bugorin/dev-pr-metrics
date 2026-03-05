package com.devprmetrics.domain.pr;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrRepository extends JpaRepository<Pr, Long> {

    Optional<Pr> findByGithubId(Long githubId);
}
