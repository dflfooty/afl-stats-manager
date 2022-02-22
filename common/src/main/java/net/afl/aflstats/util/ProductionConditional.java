package net.afl.aflstats.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import net.afl.aflstats.config.AflStatsPropertiesConfig;

@Component
@RequiredArgsConstructor
public class ProductionConditional implements Condition {

    private final AflStatsPropertiesConfig aflStatsProperties;

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		String environment = aflStatsProperties.getEnvironment();
        return environment.equalsIgnoreCase("production");
	}
}