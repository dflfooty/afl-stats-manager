package net.afl.aflstats.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix="aflstats")
public class AflStatsPropertiesConfig {
    
    private String year = "2020";
    private String baseStatsUrl = "https://afl.com.au/stats";
    private String baseFixtureUrl = "https://afl.com.au/fixture?GameWeeks=";
    private int fixtureRounds = 23;
    private String environment = "development";
}
