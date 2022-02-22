package net.afl.aflstats.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(localDateTimeToZonedDateTime);
        return mapper;
    }

    Converter<LocalDateTime, ZonedDateTime> localDateTimeToZonedDateTime = new AbstractConverter<>() {
        protected ZonedDateTime convert(LocalDateTime source) {
            ZonedDateTime target = null;
            if (source != null) {
                target = source.atZone(ZoneId.of("UTC"));
            }
            return target;
        }
    };
}
