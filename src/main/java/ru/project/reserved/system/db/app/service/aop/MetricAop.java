package ru.project.reserved.system.db.app.service.aop;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.service.MetricService;


@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class MetricAop {

    private final MetricService meterService;

    @Before("@annotation(metric)")
    @SneakyThrows
    public void before(JoinPoint joinPoint, Metric metric) {
        log.info("Request metrics");
        Object o = joinPoint.getArgs();
        meterService.sendMetricStart(metric.type(), o, metric.description());
    }

    @AfterReturning(value = "@annotation(metric)", returning = "returning")
    @SneakyThrows
    public void after(JoinPoint joinPoint, Metric metric, Object returning) {
        log.info("Response metrics aop");
        meterService.sendMetricEnd(metric.type(), returning, metric.description());
    }

    @AfterThrowing(value = "@annotation(metric)", throwing = "exception")
    public void throwApp(JoinPoint joinPoint, Metric metric, Exception exception) {
        log.info("Exception metrics aop ", exception);
        meterService.sendExceptionMetric(metric.type(), exception, metric.description());
    }


}
