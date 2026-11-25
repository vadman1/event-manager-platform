package io.github.vadman1.eventnotificator.notification;

import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventChangeListener {
    private final NotificationService notificationService;

    public EventChangeListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "${events.kafka.topic}")
    public void onEventChange(EventChangeKafkaMessage message) {
        notificationService.saveNotifications(message);
    }
}
