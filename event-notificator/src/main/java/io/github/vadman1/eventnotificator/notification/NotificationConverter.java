package io.github.vadman1.eventnotificator.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {
    private final ObjectMapper objectMapper;

    public NotificationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public NotificationDto toDto(NotificationEntity notification) {
        var payload = notification.getPayload();
        try {
            var details = new NotificationDto.Payload(
                    payload.getMessageId(), payload.getEventType(), payload.getOccurredAt(),
                    payload.getOwnerId(), payload.getChangedById(), objectMapper.readTree(payload.getPayloadJson())
            );
            return new NotificationDto(notification.getId(), payload.getEventType(), payload.getEventId(),
                    notification.getCreatedAt(), notification.isRead(), "Событие было изменено", details);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not read notification changes", e);
        }
    }
}
