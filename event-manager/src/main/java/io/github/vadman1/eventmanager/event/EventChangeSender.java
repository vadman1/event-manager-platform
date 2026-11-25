package io.github.vadman1.eventmanager.event;

import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventChangeSender {
    private static final Logger log = LoggerFactory.getLogger(EventChangeSender.class);

    private final KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate;
    private final String topic;

    public EventChangeSender(KafkaTemplate<Long, EventChangeKafkaMessage> kafkaTemplate,
                             @Value("${events.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @TransactionalEventListener
    public void sendEvent(EventChangeKafkaMessage message) {
        try {
            kafkaTemplate.send(topic, message.eventId(), message)
                    .whenComplete((result, error) -> {
                        if (error != null) {
                            log.error("Failed to publish event change {}", message.messageId(), error);
                        }
                    });
        } catch (RuntimeException e) {
            log.error("Failed to publish event change {}", message.messageId(), e);
        }
    }
}
