package ru.project.reserved.system.db.app.service.service;


import ru.project.reserved.system.db.app.service.dto.type.MetricType;

public interface MetricService {

    void sendMetricStart(MetricType metricType, Object inputObject, String description);

    void sendMetricEnd(MetricType metricType, Object returning, String description);

    void sendExceptionMetric(MetricType metricType, Exception e, String description);
}
