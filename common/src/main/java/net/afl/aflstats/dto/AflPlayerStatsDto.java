package net.afl.aflstats.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AflPlayerStatsDto {
    private int round;
    private String name;
    String team;
    private int jumperNo;
    private int kicks;
    private int handballs;
    private int disposals;
    private int marks;
    private int hitouts;
    private int freesFor;
    private int freesAgainst;
    private int tackles;
    private int goals;
    private int behinds;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
