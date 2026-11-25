package io.github.vadman1.eventnotificator.notification;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notification_event_payloads")
public class NotificationPayloadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID messageId;
    private String eventType;
    private Long eventId;
    private OffsetDateTime occurredAt;
    private Long ownerId;
    private Long changedById;
    private String payloadJson;

    protected NotificationPayloadEntity() {
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getEventType() {
        return eventType;
    }

    public Long getEventId() {
        return eventId;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getChangedById() {
        return changedById;
    }

    public String getPayloadJson() {
        return payloadJson;
    }
}
