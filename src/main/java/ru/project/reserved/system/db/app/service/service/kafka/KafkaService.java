package ru.project.reserved.system.db.app.service.service.kafka;


import org.springframework.kafka.support.Acknowledgment;

public interface KafkaService {

    void getMessageGroupDataBase(String topic, String key, String message);
}
