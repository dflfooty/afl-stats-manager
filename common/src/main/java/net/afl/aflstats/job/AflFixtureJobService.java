package net.afl.aflstats.job;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.afl.aflstats.service.AflFixtureService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AflFixtureJobService {

    private final AflFixtureService aflFixtureService;

    public void run() {

        log.info("AflFixtureJobService start");

        aflFixtureService.scrapeAndSaveFixtures();

        log.info("AflFixtureJobService end");
    }   
}
