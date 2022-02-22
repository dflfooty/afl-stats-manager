package net.afl.aflstats.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AflFixtureDto {
    private int round;
    private int game;
    private String homeTeam;
    private String awayTeam;
    private String ground;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime statsLastDownload;
}
