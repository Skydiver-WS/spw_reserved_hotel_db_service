package ru.project.reserved.system.db.app.service.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Monitoring {

    @Bean
    public Counter counter(MeterRegistry meterRegistry) {
        return  Counter.builder("db_app_counter")
                .description("Total hello requests")
                .tag("method", "GET")
                .tag("status", "200")
                .register(meterRegistry);
    }
}
