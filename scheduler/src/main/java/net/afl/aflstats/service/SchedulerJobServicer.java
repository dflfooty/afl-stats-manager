package net.afl.aflstats.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.spring.annotations.Recurring;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.job.AflFixtureJobService;
import net.afl.aflstats.job.AflStatsJobService;
import net.afl.aflstats.model.entity.AflFixture;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerJobServicer {

    private final JobScheduler jobScheduler;
    private final AflStatsJobService aflStatsJobService;
    private final AflFixtureJobService aflFixtureJobService;
    private final AflFixtureService aflFixtureService;

    @Recurring(id = "schedule-fixture-scrapper", cron = "0 9 * * 3", zoneId = "Australia/Melbourne")
    @Job(name = "Schedule Fixture Scapper")
    public void scheduleFixtureScapper() {
        log.info("SchedulerJobServicer scheduleFixtureScapper: Scheduling fixture scrapper");

        jobScheduler.enqueue(() -> aflFixtureJobService.run());

        log.info("SchedulerJobServicer scheduleFixtureScapper: Job enqueued");
    }

    @Recurring(id = "schedule-live-stats-scraper", cron = "*/5 * * * *", zoneId = "Australia/Melbourne")
    @Job(name = "Schedule Live Stats Scrapper")
    public void scheduleLiveStatsScraper() {
        log.info("SchedulerJobServicer scheduleLiveStatsScraper: Scheduling live fixture scrapper");

        List<AflFixture> liveFixtures = aflFixtureService.getFixturesToScrape("live");

        if(liveFixtures != null && !liveFixtures.isEmpty()) {
            for(AflFixture liveFixture : liveFixtures) {
                log.info("SchedulerJobServicer scheduleLiveStatsScraper: Live fixture to be scrapped={}", liveFixture);
                jobScheduler.enqueue(() -> aflStatsJobService.run(liveFixture.getRound(), liveFixture.getGame()));
                log.info("SchedulerJobServicer scheduleLiveStatsScraper: Job enqueued");
            }
        } else {
            log.info("SchedulerJobServicer schedule9amStatsScrape: No fixtures to scrape");
        }
    }

    @Recurring(id = "schedule-9am-stats-scrape", cron = "0 9 * * *", zoneId = "Australia/Melbourne")
    @Job(name = "Schedule 9am Stats Scrape")
    public void schedule9amStatsScrape() {
        log.info("SchedulerJobServicer schedule9amStatsScrape: Scheduling 9am fixture scrapper");

        List<AflFixture> fixturesToScrape = null;
        
        LocalDate today = LocalDate.now(ZoneId.of("Australia/Melbourne"));
        
        log.info("SchedulerJobServicer schedule9amStatsScrape: Today={}", today.getDayOfWeek());
        
        if(today.getDayOfWeek() == DayOfWeek.MONDAY) {
            fixturesToScrape = aflFixtureService.getFixturesToScrape("monday9am");
        } else {
            fixturesToScrape = aflFixtureService.getFixturesToScrape("9am");
        }

        if(fixturesToScrape != null && !fixturesToScrape.isEmpty()) {
            for(AflFixture liveFixture : fixturesToScrape) {
                log.info("SchedulerJobServicer schedule9amStatsScrape: Fixture to be scrapped={}", liveFixture);
                jobScheduler.enqueue(() -> aflStatsJobService.run(liveFixture.getRound(), liveFixture.getGame()));
                log.info("SchedulerJobServicer schedule9amStatsScrape: Job enqueued");
            }
        } else {
            log.info("SchedulerJobServicer schedule9amStatsScrape: No fixtures to scrape");
        }
    }

    @Recurring(id = "schedule-5pm-stats-scrape", cron = "0 17 * * *", zoneId = "Australia/Melbourne")
    @Job(name = "Schedule 5pm Stats Scrape")
    public void schedule5pmStatsScrape() {
        log.info("SchedulerJobServicer schedule5pmStatsScrape: Scheduling 5pm fixture scrapper");

        List<AflFixture> fixturesToScrape = aflFixtureService.getFixturesToScrape("5pm");

        if(fixturesToScrape != null && !fixturesToScrape.isEmpty()) {
            for(AflFixture liveFixture : fixturesToScrape) {
                log.info("SchedulerJobServicer schedule5pmStatsScrape: Fixture to be scrapped={}", liveFixture);
                jobScheduler.enqueue(() -> aflStatsJobService.run(liveFixture.getRound(), liveFixture.getGame()));
                log.info("SchedulerJobServicer schedule5pmStatsScrape: Job enqueued");
            }
        } else {
            log.info("SchedulerJobServicer schedule5pmStatsScrape: No fixtures to scrape");
        }
    }

    @Recurring(id = "schedule-5pm-stats-scrape", cron = "0 23 * * *", zoneId = "Australia/Melbourne")
    @Job(name = "Schedule 5pm Stats Scrape")
    public void schedule11pmStatsScrape() {
        log.info("SchedulerJobServicer schedule11pmStatsScrape: Scheduling 5pm fixture scrapper");

        List<AflFixture> fixturesToScrape = aflFixtureService.getFixturesToScrape("11pm");
        
        if(fixturesToScrape != null && !fixturesToScrape.isEmpty()) {
            for(AflFixture liveFixture : fixturesToScrape) {
                log.info("SchedulerJobServicer schedule11pmStatsScrape: Fixture to be scrapped={}", liveFixture);
                jobScheduler.enqueue(() -> aflStatsJobService.run(liveFixture.getRound(), liveFixture.getGame()));
                log.info("SchedulerJobServicer schedule11pmStatsScrape: Job enqueued");
            }
        } else {
            log.info("SchedulerJobServicer schedule11pmStatsScrape: No fixtures to scrape");
        }
    } 
}
