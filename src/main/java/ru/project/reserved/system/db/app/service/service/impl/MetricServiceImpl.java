package ru.project.reserved.system.db.app.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.dto.type.MetricType;
import ru.project.reserved.system.db.app.service.service.MetricService;


import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricServiceImpl implements MetricService {

    private final MeterRegistry meterRegistry;

    @Override
    @SneakyThrows
    public void sendMetricStart(MetricType metricType, Object inputObject, String description) {
        log.info("Send metric start");
        Counter counter = Counter.builder(metricType.getType() + "_start")
                .description(description)
                .tag("body", new ObjectMapper().writeValueAsString(inputObject))
                .register(meterRegistry);
        counter.increment();

    }

    @Override
    @SneakyThrows
    public void sendMetricEnd(MetricType metricType, Object returning, String description) {
        log.info("Send metrics end");
        if (Objects.nonNull(returning) && returning instanceof ResponseEntity<?> r){
            Counter counter = Counter.builder(metricType.getType() + "_end")
                    .description(description)
                    .tag("code", String.valueOf(r.getStatusCode().value()))
                    .tag("body", new ObjectMapper().writeValueAsString(r.getBody()))
                    .register(meterRegistry);
            counter.increment();
        }
    }

    @Override
    public void sendExceptionMetric(MetricType metricType, Exception exception, String description) {
        log.info("Exception metrics");
        String code = "500";
        Counter counter = Counter.builder(metricType.getType() + "_error")
                .description(description)
                .tag("code", code)
                .tag("error", exception.getMessage())
                .register(meterRegistry);
        counter.increment();
    }
}
