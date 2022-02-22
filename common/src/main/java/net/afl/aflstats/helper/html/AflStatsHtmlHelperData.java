package net.afl.aflstats.helper.html;

import java.util.List;

public class AflStatsHtmlHelperData {

    public List<Stats> homeTeamStats;
    public List<Stats> awayTeamStats;

    public static class Stats {
        public String name;
        public int jumper;
        public int kicks;
        public int handballs;
        public int disposals;
        public int marks;
        public int hitouts;
        public int freesFor;
        public int freesAgainst;
        public int tackles;
        public int goals;
        public int behinds;
    }
}
