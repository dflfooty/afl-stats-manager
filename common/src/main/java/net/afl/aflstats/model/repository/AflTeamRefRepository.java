package net.afl.aflstats.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.afl.aflstats.model.entity.AflTeamRef;

public interface AflTeamRefRepository extends JpaRepository<AflTeamRef, Long> {}
