package net.afl.aflstats.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.config.AflStatsPropertiesConfig;
import net.afl.aflstats.dto.AflFixtureDto;
import net.afl.aflstats.dto.AflFixtureWithStatsDto;
import net.afl.aflstats.helper.html.AflFixtureHtmlHelper;
import net.afl.aflstats.helper.html.AflFixtureHtmlHelperData;
import net.afl.aflstats.helper.html.AflGameCompletionHtmlHelper;
import net.afl.aflstats.model.entity.AflFixture;
import net.afl.aflstats.model.repository.AflFixtureRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AflFixtureService {

    private final AflFixtureRepository aflFixtureRepository;
    private final AflGameCompletionHtmlHelper aflGameCompletionHtmlHelper;
    private final AflFixtureHtmlHelper aflFixtureHtmlHelper;
    private final AflReferenceService aflReferenceService;
    private final ModelMapper modelMapper;
    private final AflStatsPropertiesConfig aflStatsProperties;

    public List<AflFixtureDto> getFixtures() {
        List<AflFixture> aflFixutres = aflFixtureRepository.findAll();
        return aflFixutres.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public AflFixtureWithStatsDto getFixtureWithStats(int round, int game) {
        AflFixture aflFixture = aflFixtureRepository.findByRoundAndGame(round, game);
        return convertWithStatsToDto(aflFixture);
    }

    public AflFixture setStatLastDownload(AflFixture fixture) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        fixture.setStatsLastDownload(now);
        return aflFixtureRepository.save(fixture);
    }

    public void scrapeAndSaveFixtures() {
        log.info("AflFixtureService getAndSaveFixures start");

        Map<String, String> refMap = aflReferenceService.getAflTeamRefMapLongNameToShort();
        List<AflFixture> fixtures = new ArrayList<>();

        for(int i = 1; i <= aflStatsProperties.getFixtureRounds(); i++) {
            String fixtureUrl = aflStatsProperties.getBaseFixtureUrl() + i;

            List<AflFixtureHtmlHelperData> roundFixtures = aflFixtureHtmlHelper.getFixtures(fixtureUrl);

            for(AflFixtureHtmlHelperData roundFixture : roundFixtures) {
                AflFixture fixture = aflFixtureRepository.findByRoundAndGame(i, roundFixture.game);
                if(fixture == null) {
                    fixture = new AflFixture();

                    fixture.setRound(i);
                    fixture.setGame(roundFixture.game);
                }

                fixture.setHomeTeam(refMap.get(roundFixture.homeTeam));
                fixture.setAwayTeam(refMap.get(roundFixture.awayTeam));
                fixture.setStartTime(roundFixture.startTime);

                fixtures.add(fixture);
            }
        }
        
        List<AflFixture> savedFixtures = aflFixtureRepository.saveAll(fixtures);
        
        log.debug("AflFixtureService getAndSaveFixture; scrapped fixtures={}", fixtures);
        log.debug("AflFixtureService getAndSaveFixture; saved fixtures={}", savedFixtures);
        log.info("AflFixtureService getAndSaveFixture end");
    }

    public void completeFixtures() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        log.info("AflFixtureService: completeFixtures, start, now={}", now);

        List<AflFixture> fixtures = aflFixtureRepository.findLiveFixtures(now);

        List<AflFixture> completeFixtures = new ArrayList<>();

        for (AflFixture fixture : fixtures) {
            if (checkCompleteFixture(fixture)) {
                fixture.setEndTime(now);
                completeFixtures.add(fixture);

                log.info("AflFixtureService: completedFixture, fixture={}", fixture);
            }
        }

        aflFixtureRepository.saveAll(completeFixtures);

        log.info("AflFixtureService: completeFixtures, end");
    }

    public AflFixture getFixtureForGame(int round, int game) {
        return aflFixtureRepository.findByRoundAndGame(round, game);
    }

    public List<AflFixture> getFixturesToScrape(String mode) {
    
        List<AflFixture> fixturesToScrape = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));

        switch (mode) {
            case "live":
                fixturesToScrape.addAll(aflFixtureRepository.findLiveFixtures(now));
                fixturesToScrape.addAll(aflFixtureRepository.findRecentlyCompletedFixutres(now));
                break;
            case "monday9am":
                LocalDateTime days7Ago = now.minusDays(7);
                fixturesToScrape.addAll(aflFixtureRepository.findCompletedSinceFixutres(days7Ago));
                break;
            case "9am":
                LocalDateTime days1Ago = now.minusDays(1);
                fixturesToScrape.addAll(aflFixtureRepository.findCompletedSinceFixutres(days1Ago));
                break;
            case "5pm":
            case "11pm":
                LocalDateTime today = now.with(LocalTime.MIN);
                fixturesToScrape.addAll(aflFixtureRepository.findCompletedSinceFixutres(today));
                break;
            default:
                break;
        }
        
        return fixturesToScrape;
    }

    private AflFixtureDto convertToDto(AflFixture aflFixture) {
        return modelMapper.map(aflFixture, AflFixtureDto.class);
    }

    private AflFixtureWithStatsDto convertWithStatsToDto(AflFixture aflFixture) {
        return modelMapper.map(aflFixture, AflFixtureWithStatsDto.class);
    }

    private boolean checkCompleteFixture(AflFixture fixture) {

        boolean fixtureComplete = false;

        String roundStr = String.format("%02d", fixture.getRound());
        String gameStr = String.format("%02d", fixture.getGame());

        String gameUrl = aflStatsProperties.getBaseStatsUrl() + "/AFL" + aflStatsProperties.getYear() + roundStr + gameStr;

        fixtureComplete = aflGameCompletionHtmlHelper.checkGameCompletion(gameUrl);

        return fixtureComplete;
    }

}
