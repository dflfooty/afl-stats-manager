package net.afl.aflstats.helper.html.webdriver;

import java.util.concurrent.TimeUnit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.stereotype.Component;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import net.afl.aflstats.config.WebdriverPropertiesConfig;

@Component
@RequiredArgsConstructor
public class ScrapperWebDriver {

    private final WebdriverPropertiesConfig webdriverProperties;

    public WebDriver getHtmlUnitWebDriver() {

        BrowserVersion browserVersion = new BrowserVersion.BrowserVersionBuilder(BrowserVersion.CHROME)
        .setApplicationName(webdriverProperties.getApplication().getName())
        .setApplicationVersion(webdriverProperties.getApplication().getVersion())
        .setUserAgent(webdriverProperties.getUserAgent())
        .build();

        WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME) {
            @Override
            protected WebClient newWebClient(BrowserVersion version) {
                WebClient webClient = super.newWebClient(version);
                webClient.getOptions().setThrowExceptionOnScriptError(false);
                webClient.getOptions().setCssEnabled(false);
                webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                return webClient;
            }
        };

        if (webdriverProperties.isUseProxy()) {
            driver = new HtmlUnitDriver(browserVersion) {
                @Override
                protected WebClient newWebClient(BrowserVersion version) {
                    WebClient webClient = super.newWebClient(version);
                    webClient.getOptions().setThrowExceptionOnScriptError(false);
                    webClient.getOptions().setCssEnabled(false);
                    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

                    String[] proxyUserInfo = webdriverProperties.getProxyUrl().getUserInfo().split(":");

                    webClient.getOptions().setProxyConfig(
                        new ProxyConfig(webdriverProperties.getProxyUrl().getHost(),
                            webdriverProperties.getProxyUrl().getPort(),
                            webdriverProperties.getProxyUrl().getProtocol()));
                    webClient.getCredentialsProvider().setCredentials(
                        new AuthScope(webdriverProperties.getProxyUrl().getHost(),
                            webdriverProperties.getProxyUrl().getPort()),
                            new UsernamePasswordCredentials(proxyUserInfo[0], proxyUserInfo[1]));
                    return webClient;
                }
            };
        }

        driver.manage().timeouts().implicitlyWait(webdriverProperties.getWait(), TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(webdriverProperties.getTimeout(), TimeUnit.SECONDS);

        return driver;
    }

    public WebDriver getChromeWebDriver() {
 
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--user-agent="+webdriverProperties.getUserAgent());

        return new ChromeDriver(chromeOptions);
    }
    
}
