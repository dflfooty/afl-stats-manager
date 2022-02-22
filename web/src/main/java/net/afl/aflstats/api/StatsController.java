package net.afl.aflstats.api;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.afl.aflstats.dto.AflFixtureWithStatsDto;
import net.afl.aflstats.dto.AflPlayerStatsDto;
import net.afl.aflstats.service.AflFixtureService;
import net.afl.aflstats.service.AflStatsService;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {
    
    private final AflStatsService aflStatsService;
    private final AflFixtureService aflFixtureService;

    @GetMapping
    public List<AflPlayerStatsDto> getStats() {
        return aflStatsService.getStats();
    }

    @GetMapping("/{round}")
    public List<AflPlayerStatsDto> getStatsForRound(@PathVariable("round") int round) {
        return aflStatsService.getStatsForRound(round);
    }

    @GetMapping("/{round}/{game}")
    public List<AflPlayerStatsDto> getStatsForRoundAndGame(@PathVariable("round") int round, @PathVariable("game") int game) {
        AflFixtureWithStatsDto fixtureWithStats = aflFixtureService.getFixtureWithStats(round, game);
        return Stream.of(fixtureWithStats.getHomeTeamStats(), fixtureWithStats.getAwayTeamStats())
            .flatMap(Collection::stream).collect(Collectors.toList());
    }
}
