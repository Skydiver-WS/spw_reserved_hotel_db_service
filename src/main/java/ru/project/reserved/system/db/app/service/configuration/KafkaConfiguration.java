//package ru.project.reserved.system.db.app.service.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.config.TopicBuilder;
//import org.springframework.kafka.core.*;
//import ru.project.reserved.system.db.app.service.properties.KafkaConsumerProperties;
//import ru.project.reserved.system.db.app.service.properties.KafkaProducerProperties;
//
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@Slf4j
//@RequiredArgsConstructor
//public class KafkaConfiguration {
//
//    private final KafkaProducerProperties kafkaProducerProperties;
//    private final KafkaConsumerProperties kafkaConsumerProperties;
//    private final KafkaAdmin kafkaAdmin;
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        log.info("Creating Kafka template");
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        log.info("Creating producer factory");
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getUrl());
//        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        log.info("Creating consumer factory");
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getUrl());
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getMessageGroupId());
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        log.info("Creating concurrent kafkaListenerContainerFactory");
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//
//
//    @PostConstruct
//    public void createTopics() {
//        log.info("Creating topics");
//        Arrays.asList(kafkaProducerProperties.getTopicList())
//                .forEach(topic -> kafkaAdmin.createOrModifyTopics(
//                        TopicBuilder.name(topic)
//                                .partitions(3)
//                                .replicas(1)
//                                .build()
//                ));
//    }
//
//    @Bean
//    public String[] kafkaConsumerTopics(){
//        log.info("Creating kafka topics");
//        return kafkaConsumerProperties.getTopicList();
//    }
//}
