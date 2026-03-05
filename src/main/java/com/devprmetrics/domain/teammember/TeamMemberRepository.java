package com.devprmetrics.domain.teammember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    Page<TeamMember> findByTeamId(Long teamId, Pageable pageable);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
}
