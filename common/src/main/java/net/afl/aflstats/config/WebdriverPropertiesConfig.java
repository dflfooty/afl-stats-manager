package net.afl.aflstats.config;

import java.net.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix="webdriver")
public class WebdriverPropertiesConfig {
 
    @Data
    public static class Application {
        private String name = "default";
        private String version = "1";
    }

    private boolean enable = false;
    private int timeout = 10;
    private int wait = 10;
    private Application application;
    private String userAgent = "default/1";
    private boolean useProxy = false;
    private URL proxyUrl;
}
