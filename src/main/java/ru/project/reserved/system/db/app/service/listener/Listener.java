package ru.project.reserved.system.db.app.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import ru.project.reserved.system.db.app.service.properties.KafkaConsumerProperties;
import ru.project.reserved.system.db.app.service.service.kafka.KafkaService;

import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class Listener {

    private final KafkaService kafkaService;
    private final KafkaConsumerProperties kafkaConsumerProperties;


    @KafkaListener(groupId = "${spring.kafka.consumer.topic.kafkaMessageGroupId}",
            topics = "#{@kafkaConsumerTopics}",
            containerFactory = "kafkaListenerContainerFactory")
    public void kafkaListener(String message,
                              @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        if (Arrays.asList(kafkaConsumerProperties.getTypeKey()).contains(key)) {
            log.info("Get key {}, message: {}", key, message);
            kafkaService.getMessageGroupDataBase(key, message);
        }
    }
}
