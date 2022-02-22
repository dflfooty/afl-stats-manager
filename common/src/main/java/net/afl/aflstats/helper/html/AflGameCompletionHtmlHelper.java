package net.afl.aflstats.helper.html;

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
public class AflGameCompletionHtmlHelper {

    private final ScrapperWebDriver scrapperWebDriver;


    public boolean checkGameCompletion(String gameUrl) {

        boolean gameCompleted = false;

        log.info("AflGameCompletionCheckerHtmlHelper: checkGameCompletion, start, gameUrl={}", gameUrl);

        WebDriver driver = scrapperWebDriver.getHtmlUnitWebDriver();

        driver.get(gameUrl);
        
        if (driver.findElements(By.className("styles__Scoreboard-sc-14r16wm-0")).size() != 0) {
            WebElement scorecard = driver.findElement(By.className("styles__Scoreboard-sc-14r16wm-0"));
            if (scorecard.findElement(By.className("styles__State-lxmyn6-2")).getText().equals("Full Time")) {
                gameCompleted = true;
            }
        }

        driver.quit();

        log.info("AflGameCompletionCheckerHtmlHelper: checkGameCompletion, end, gameUrl={}, complete={}", gameUrl,
                gameCompleted);

        return gameCompleted;
    }
}
