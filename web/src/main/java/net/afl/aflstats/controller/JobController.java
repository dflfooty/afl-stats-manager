package net.afl.aflstats.controller;

import org.jobrunr.scheduling.JobScheduler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.dto.AflStatsJobDto;
import net.afl.aflstats.job.AflFixtureJobService;
import net.afl.aflstats.job.AflStatsJobService;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {
    
    private final JobScheduler jobScheduler;
    private final AflStatsJobService aflStatsJobService;
    private final AflFixtureJobService aflFixtureJobService;

    @PostMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> enqueueStatsJob(@RequestBody AflStatsJobDto body) {
        jobScheduler.enqueue(() -> aflStatsJobService.run(body.getRound(), body.getGame()));
        
        log.info("Afl stats job submitted: {}", body);
        
        return new ResponseEntity<>("AflStatsJob is enqueued", HttpStatus.OK);
    }

    @PostMapping(value = "/fixtures", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> enqueueFixturesJob() {
        jobScheduler.enqueue(() -> aflFixtureJobService.run());

        log.info("Afl fixture job submitted");

        return new ResponseEntity<>("AflFixtureJob is enqueued", HttpStatus.OK);
    }
}
