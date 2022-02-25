package net.afl.aflstats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SchedulerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}
}
