package ru.project.reserved.system.db.app.service.service.kafka;

import java.util.UUID;

public interface KafkaService {

    void getMessageGroupDataBase(String topic, String key, String message);
}
