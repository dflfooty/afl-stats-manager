package net.afl.aflstats.dto;

import lombok.Data;

@Data
public class AflStatsJobDto {

    private int round;
    private int game;
    private Boolean retry;

}
