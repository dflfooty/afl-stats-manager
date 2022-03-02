package net.afl.aflstats.helper.html;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.helper.html.webdriver.ScrapperWebDriver;

@Component
@RequiredArgsConstructor
@Slf4j
public class AflFixtureHtmlHelper {
    
    private final ScrapperWebDriver scrapperWebDriver;

    public List<AflFixtureHtmlHelperData> getFixtures(String fixtureUrl) {
        List<AflFixtureHtmlHelperData> fixtures = new ArrayList<>();

        log.info("AflStatsHtmlHelper getFixtures start fixutureUrl={}", fixtureUrl);

        WebDriver driver = scrapperWebDriver.getChromeWebDriver();

        driver.get(fixtureUrl);

        List<WebElement> fixtureRows = driver.findElements(By.className("match-list__details"));

        int game = 1;

		for(WebElement fixtureRow : fixtureRows) {
            AflFixtureHtmlHelperData fixture = new AflFixtureHtmlHelperData();
            fixture.game = game;

            List<WebElement> teams = fixtureRow.findElements(By.className("match-team__name"));
            String homeTeam = teams.get(0).getAttribute("textContent").trim();
            String awayTeam = teams.get(1).getAttribute("textContent").trim();
            fixture.homeTeam = homeTeam;
            fixture.awayTeam = awayTeam;

            try {
                long unixDateTime = Long.parseLong(fixtureRow.findElement(By.className("match-scheduled__time")).getAttribute("data-date"));
                fixture.startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(unixDateTime), ZoneId.systemDefault());
            } catch (NumberFormatException e) {
                log.info("Game start time TBD");
            }
            
            String ground = fixtureRow.findElement(By.className("match-scheduled__venue")).getText().replaceAll("[^a-zA-Z0-9]", "");
            fixture.ground = ground;

            fixtures.add(fixture);

            game++;
        }

        driver.quit();

        log.debug("AflStatsHtmlHelper getStats: stats={}", fixtures);
        log.info("AflStatsHtmlHelper getFixtures end");

        return fixtures;
    }
}
