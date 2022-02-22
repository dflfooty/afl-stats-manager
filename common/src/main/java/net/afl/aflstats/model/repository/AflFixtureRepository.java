package net.afl.aflstats.model.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.afl.aflstats.model.entity.AflFixture;

public interface AflFixtureRepository extends JpaRepository<AflFixture, Long> {

    List<AflFixture> findByRound(int round);

    AflFixture findByRoundAndGame(int round, int game);

    @Query("select f from AflFixture f where f.startTime < :time and f.endTime is null")
    List<AflFixture> findLiveFixtures(@Param("time") LocalDateTime time);
    
    @Query("select f from AflFixture f where f.startTime < :time and f.endTime > f.statsLastDownload")
    List<AflFixture> findRecentlyCompletedFixutres(@Param("time") LocalDateTime time);

    @Query("select f from AflFixture f where f.endTime > :time")
    List<AflFixture> findCompletedSinceFixutres(@Param("time") LocalDateTime time);
}
