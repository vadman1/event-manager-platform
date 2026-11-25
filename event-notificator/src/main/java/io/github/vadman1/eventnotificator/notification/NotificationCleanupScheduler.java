package io.github.vadman1.eventnotificator.notification;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationCleanupScheduler {
    private final NotificationService notificationService;

    public NotificationCleanupScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(fixedDelayString = "${notifications.cleanup.fixed-delay-ms}")
    public void deleteExpired() {
        notificationService.deleteExpired();
    }
}
