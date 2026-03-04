package com.devprmetrics.domain.pr;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrRepository extends JpaRepository<Pr, Long> {

    default Optional<Pr> findById(int id) {
        return findById((long) id);
    }
}
