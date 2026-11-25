package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.apache.kafka.clients.admin.NewTopic;

@Configuration
public class EventKafkaConfiguration {
    @Bean
    KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate(KafkaProperties properties, SslBundles sslBundles) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(sslBundles)));
    }

    @Bean
    NewTopic eventChangesTopic(@Value("${events.kafka.topic}") String topic) {
        return TopicBuilder.name(topic).partitions(1).replicas(1).build();
    }
}
