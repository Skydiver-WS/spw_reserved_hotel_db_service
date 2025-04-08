package ru.project.reserved.system.db.app.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ru.project.reserved.system.db.app.service.properties.KafkaConsumerProperties;
import ru.project.reserved.system.db.app.service.service.kafka.KafkaService;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
@RequiredArgsConstructor
@Profile({"!uploadRooms"})
public class Listener {

    private final KafkaService kafkaService;
    private final KafkaConsumerProperties kafkaConsumerProperties;


    @KafkaListener(groupId = "${spring.kafka.consumer.topic.kafkaMessageGroupId}",
            topics = "#{@kafkaConsumerTopics}",
            containerFactory = "kafkaListenerContainerFactory")
    public void kafkaListener(String message,
                              @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                              @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        if (Arrays.asList(kafkaConsumerProperties.getTopicList()).contains(topic)) {
            log.debug("Get topic {}, key: {}", topic, key);
            kafkaService.getMessageGroupDataBase(topic, key, message);
        }
    }
}
