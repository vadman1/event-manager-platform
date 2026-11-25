package io.github.vadman1.eventnotificator.notification;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "notifications")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private boolean isRead;
    private OffsetDateTime createdAt;
    private OffsetDateTime readAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payload_id")
    private NotificationPayloadEntity payload;

    protected NotificationEntity() {
    }

    public NotificationEntity(Long userId, OffsetDateTime createdAt, NotificationPayloadEntity payload) {
        this.userId = userId;
        this.createdAt = createdAt;
        this.payload = payload;
    }

    public Long getId() {
        return id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public NotificationPayloadEntity getPayload() {
        return payload;
    }

    public boolean isRead() {
        return isRead;
    }
}
