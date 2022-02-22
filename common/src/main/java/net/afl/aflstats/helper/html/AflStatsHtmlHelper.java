package net.afl.aflstats.helper.html;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.afl.aflstats.helper.html.webdriver.ScrapperWebDriver;

@Component
@AllArgsConstructor
@Slf4j
public class AflStatsHtmlHelper {

    private final ScrapperWebDriver scrapperWebDriver;

    public AflStatsHtmlHelperData getStats(String statsUrl) {

        log.info("AflStatsHtmlHelper getStats start statsUrl={}", statsUrl);

        AflStatsHtmlHelperData stats = new AflStatsHtmlHelperData();

        WebDriver driver = scrapperWebDriver.getHtmlUnitWebDriver();
        driver.get(statsUrl);

        List<WebElement> homeTeamStatsHtml = driver.findElements(By.className("fiso-mcfootball-match-player-stats-tables__team")).get(0)
            .findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        List<WebElement> awayTeamStatsHtml = driver.findElements(By.className("fiso-mcfootball-match-player-stats-tables__team")).get(1)
            .findElement(By.tagName("tbody")).findElements(By.tagName("tr"));

        stats.homeTeamStats = getStatsFromHtml(homeTeamStatsHtml);
        stats.awayTeamStats = getStatsFromHtml(awayTeamStatsHtml);

        driver.quit();

        log.debug("AflStatsHtmlHelper getStats: stats={}", stats);
        log.info("AflStatsHtmlHelper getStats end");

        return stats;
    }

    private List<AflStatsHtmlHelperData.Stats> getStatsFromHtml(List<WebElement> statsHtml) {

        List<AflStatsHtmlHelperData.Stats> stats = new ArrayList<>();

        for (WebElement statsHtmlRec : statsHtml) {

            List<WebElement> statsRec = statsHtmlRec.findElements(By.tagName("td"));

            AflStatsHtmlHelperData.Stats playerStats = new AflStatsHtmlHelperData.Stats();

            playerStats.name = statsRec.get(1).getText();
            playerStats.jumper = Integer.parseInt(statsRec.get(0).getText());
            playerStats.kicks = Integer.parseInt(statsRec.get(4).getText());
            playerStats.handballs = Integer.parseInt(statsRec.get(5).getText());
            playerStats.disposals = Integer.parseInt(statsRec.get(6).getText());
            playerStats.marks = Integer.parseInt(statsRec.get(7).getText());
            playerStats.hitouts = Integer.parseInt(statsRec.get(8).getText());
            playerStats.freesFor = Integer.parseInt(statsRec.get(9).getText());
            playerStats.freesAgainst = Integer.parseInt(statsRec.get(10).getText());
            playerStats.tackles = Integer.parseInt(statsRec.get(11).getText());
            playerStats.goals = Integer.parseInt(statsRec.get(2).getText());
            playerStats.behinds = Integer.parseInt(statsRec.get(3).getText());

            stats.add(playerStats);
        }

        return stats;
    }
}
