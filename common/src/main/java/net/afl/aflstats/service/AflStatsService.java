package net.afl.aflstats.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.dto.AflPlayerStatsDto;
import net.afl.aflstats.helper.html.AflStatsHtmlHelper;
import net.afl.aflstats.helper.html.AflStatsHtmlHelperData;
import net.afl.aflstats.model.entity.AflFixture;
import net.afl.aflstats.model.entity.AflPlayerStats;
import net.afl.aflstats.model.repository.AflPlayerStatsRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AflStatsService {

    private final AflPlayerStatsRepository aflPlayerStatsRepository;
    private final AflStatsHtmlHelper aflStatsScraper;
    private final ModelMapper modelMapper;

    @Value("${aflstats.year:2020}")
    private String year;

    @Value("${aflstats.base-stats-url:https://afl.com.au/stats}")
    private String baseStatsUrl;

    public List<AflPlayerStatsDto> getStats() {
        List<AflPlayerStats> stats = aflPlayerStatsRepository.findAll();
        return stats.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<AflPlayerStatsDto> getStatsForRound(int round) {
        List<AflPlayerStats> stats = aflPlayerStatsRepository.findByRound(round);
        return stats.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public void scrapeAndSaveStats(AflFixture fixture) {

        log.info("AflStatsService getAndSaveStats start: fixture={}", fixture);

        saveStats(scrapeStats(fixture));

        log.info("AflStatsService getAndSaveStats end: fixture={}", fixture);
    }

    public List<AflPlayerStats> scrapeStats(AflFixture fixture) {

        log.info("AflStatsService getStats start");

        List<AflPlayerStats> playerStats = new ArrayList<>();

        String roundStr = String.format("%02d", fixture.getRound());
        String gameStr = String.format("%02d", fixture.getGame());

        String fullStatsUrl = baseStatsUrl + "/AFL" + year + roundStr + gameStr + "/playerstats";

        log.info("AflStatsService getStats fullStatsUrl={}", fullStatsUrl);

        AflStatsHtmlHelperData statsData = aflStatsScraper.getStats(fullStatsUrl);

        playerStats.addAll(extractPlayerStats(fixture, fixture.getHomeTeam(), statsData.homeTeamStats));
        playerStats.addAll(extractPlayerStats(fixture, fixture.getAwayTeam(), statsData.awayTeamStats));

        log.debug("AflStatsService getStats: playerStats={}", playerStats);
        log.info("AflStatsService getStats end");

        return playerStats;
    }

    public void saveStats(List<AflPlayerStats> stats) {

        log.info("AflStatsService saveStats start");

        for (AflPlayerStats playerStatsNew : stats) {
            AflPlayerStats playerStatsOld = aflPlayerStatsRepository.findByRoundAndTeamAndJumperNo(
                    playerStatsNew.getRound(), playerStatsNew.getTeam(), playerStatsNew.getJumperNo());

            log.debug("AflStatsService saveStats playerStatsNew={}", playerStatsNew);
            log.debug("AflStatsService saveStats playerStatsOld={}", playerStatsOld);

            if (playerStatsOld != null && !playerStatsNew.equalsLogically(playerStatsOld)) {

                playerStatsNew.setId(playerStatsOld.getId());
                playerStatsNew.setCreatedAt(playerStatsOld.getCreatedAt());

                aflPlayerStatsRepository.save(playerStatsNew);

                log.info("AflStatsService saveStats playerStatsUpdated=true");
            } else if (playerStatsOld == null) {
                aflPlayerStatsRepository.save(playerStatsNew);

                log.info("AflStatsService saveStats playerStatsCreated=true");
            } else {
                log.info("No change in players stats: id-{}, name={}, team={}", playerStatsOld.getId(),
                        playerStatsOld.getName(), playerStatsOld.getTeam());
            }
        }

        log.info("AflStatsService saveStats end");
    }

    private AflPlayerStatsDto convertToDto(AflPlayerStats stats) {
        return modelMapper.map(stats, AflPlayerStatsDto.class);
    }

    private List<AflPlayerStats> extractPlayerStats(AflFixture fixture, String team,
            List<AflStatsHtmlHelperData.Stats> playerStatsData) {

        List<AflPlayerStats> playerStats = new ArrayList<>();

        for (AflStatsHtmlHelperData.Stats statsRec : playerStatsData) {

            AflPlayerStats playerStatsRec = new AflPlayerStats();

            playerStatsRec.setRound(fixture.getRound());
            playerStatsRec.setName(statsRec.name);
            playerStatsRec.setTeam(team);
            playerStatsRec.setJumperNo(statsRec.jumper);
            playerStatsRec.setKicks(statsRec.kicks);
            playerStatsRec.setHandballs(statsRec.handballs);
            playerStatsRec.setDisposals(statsRec.disposals);
            playerStatsRec.setMarks(statsRec.marks);
            playerStatsRec.setHitouts(statsRec.hitouts);
            playerStatsRec.setFreesFor(statsRec.freesFor);
            playerStatsRec.setFreesAgainst(statsRec.freesAgainst);
            playerStatsRec.setTackles(statsRec.tackles);
            playerStatsRec.setGoals(statsRec.goals);
            playerStatsRec.setBehinds(statsRec.behinds);

            playerStatsRec.setFixture(fixture);

            playerStats.add(playerStatsRec);

        }

        return playerStats;
    }
}
