package io.github.vadman1.eventnotificator.notification;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.OffsetDateTime;
import java.util.UUID;

public record NotificationDto(Long notificationId, String type, Long eventId, OffsetDateTime createdAt,
                              boolean isRead, String message, Payload payload) {
    public record Payload(UUID messageId, String eventType, OffsetDateTime occurredAt, Long ownerId,
                          Long changedById, JsonNode changes) {
    }
}
