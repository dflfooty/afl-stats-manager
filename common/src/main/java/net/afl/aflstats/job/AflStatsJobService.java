package net.afl.aflstats.job;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.afl.aflstats.model.entity.AflFixture;
import net.afl.aflstats.service.AflFixtureService;
import net.afl.aflstats.service.AflStatsService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AflStatsJobService {

    private final AflStatsService aflStatsService;
    private final AflFixtureService aflFixtureService;

    public void run(int round, int game) {

        log.info("AflStatsJobService start: round={} game={}", round, game);

        AflFixture fixture = aflFixtureService.getFixtureForGame(round, game);
        aflStatsService.scrapeAndSaveStats(fixture);
        aflFixtureService.setStatLastDownload(fixture);

        log.info("AflStatsJobService end: round={} game={}", round, game);
    }
    
}
