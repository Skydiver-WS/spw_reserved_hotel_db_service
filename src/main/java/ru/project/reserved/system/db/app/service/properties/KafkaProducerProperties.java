package ru.project.reserved.system.db.app.service.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.kafka.producer.topic")
public class KafkaProducerProperties {
    @Value("${spring.kafka.bootstrap-servers}")
    private String url;

    private String[] topicList;

}
