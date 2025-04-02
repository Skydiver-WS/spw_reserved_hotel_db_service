package ru.project.reserved.system.db.app.service.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.properties.KafkaConsumerProperties;
import ru.project.reserved.system.db.app.service.service.impl.UploadRoomsService;

import java.util.Arrays;


@RequiredArgsConstructor
@Service
@Slf4j
@Profile({"uploadRoomsu"})
public class ListenerRooms {
    private final UploadRoomsService kafkaService;
    private final KafkaConsumerProperties kafkaConsumerProperties;



    @KafkaListener(groupId = "${spring.kafka.consumer.topic.kafkaMessageGroupId}",
            topics = "#{@kafkaConsumerTopics}",
            containerFactory = "kafkaListenerContainerFactory")
    public void kafkaListener(String message,
                              @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                              @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {
        if (Arrays.asList(kafkaConsumerProperties.getTopicList()).contains(topic)) {
            kafkaService.getMessageGroupDataBase(topic, key, message);
        }
    }
}
