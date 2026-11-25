package io.github.vadman1.eventnotificator.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    private final NotificationRepository notifications;
    private final NotificationPayloadRepository payloads;
    private final ObjectMapper objectMapper;
    private final NotificationConverter converter;

    public NotificationService(NotificationRepository notifications, NotificationPayloadRepository payloads,
                               ObjectMapper objectMapper, NotificationConverter converter) {
        this.notifications = notifications;
        this.payloads = payloads;
        this.objectMapper = objectMapper;
        this.converter = converter;
    }

    @Transactional
    public void saveNotifications(EventChangeKafkaMessage message) {
        validateMessage(message);
        if (message.subscribers().isEmpty()) {
            return;
        }

        String changesJson;
        try {
            changesJson = objectMapper.writeValueAsString(message.changes());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize event changes", e);
        }

        if (payloads.insertIfAbsent(message, changesJson) == 0) {
            return;
        }
        var payload = payloads.findByMessageId(message.messageId()).orElseThrow();
        var now = OffsetDateTime.now();
        var entries = message.subscribers().stream().distinct()
                .map(userId -> new NotificationEntity(userId, now, payload))
                .toList();
        notifications.saveAll(entries);
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> findUnread(Long userId) {
        return notifications.findAllByUserIdAndIsReadFalseOrderByCreatedAtDescIdDesc(userId)
                .stream().map(converter::toDto).toList();
    }

    @Transactional
    public void markAsRead(Long userId, List<Long> ids) {
        notifications.markAsRead(userId, ids, OffsetDateTime.now());
    }

    @Transactional
    public void deleteExpired() {
        notifications.deleteCreatedBefore(OffsetDateTime.now().minusDays(7));
        payloads.deleteOrphans();
    }

    private void validateMessage(EventChangeKafkaMessage message) {
        if (message.messageId() == null || message.eventId() == null || message.eventType() == null
                || message.eventType().isBlank() || message.eventType().length() > 255
                || message.ownerId() == null || message.occurredAt() == null || message.subscribers() == null
                || message.subscribers().stream().anyMatch(Objects::isNull)
                || message.changes() == null || message.changes().isEmpty()
                || message.changes().stream().anyMatch(change -> change == null
                    || change.field() == null || change.field().isBlank())) {
            throw new IllegalArgumentException("Invalid event change message");
        }
    }
}
