package net.afl.aflstats.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.afl.aflstats.model.entity.AflPlayerStats;

public interface AflPlayerStatsRepository extends JpaRepository<AflPlayerStats, Long> {

    List<AflPlayerStats> findByRound(int round);
    AflPlayerStats findByRoundAndTeamAndJumperNo(int round, String team, int jumperNo);

}
