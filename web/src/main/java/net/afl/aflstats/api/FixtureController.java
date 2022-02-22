package net.afl.aflstats.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.afl.aflstats.dto.AflFixtureDto;
import net.afl.aflstats.dto.AflFixtureWithStatsDto;
import net.afl.aflstats.service.AflFixtureService;

@RestController
@RequestMapping("/api/fixtures")
@RequiredArgsConstructor
public class FixtureController {

    private final AflFixtureService fixtureService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AflFixtureDto> getFixtures() {
        return fixtureService.getFixtures();
    }

    @GetMapping("/stats/{round}/{game}")
    @ResponseStatus(HttpStatus.OK)
    public AflFixtureWithStatsDto getFixutreWithStats(@PathVariable("round") int round,
            @PathVariable("game") int game) {
        return fixtureService.getFixtureWithStats(round, game);
    }
}
