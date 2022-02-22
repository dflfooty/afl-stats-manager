package net.afl.aflstats.dto;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AflFixtureWithStatsDto {
    private int round;
    private int game;
    private String homeTeam;
    private String awayTeam;
    private String ground;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private ZonedDateTime statsLastDownload;
    private List<AflPlayerStatsDto> homeTeamStats;
    private List<AflPlayerStatsDto> awayTeamStats;
}
