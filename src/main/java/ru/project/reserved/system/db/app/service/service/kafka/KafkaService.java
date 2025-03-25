package ru.project.reserved.system.db.app.service.service.kafka;


public interface KafkaService {

    void getMessageGroupDataBase(String topic, String key, String message);
}
