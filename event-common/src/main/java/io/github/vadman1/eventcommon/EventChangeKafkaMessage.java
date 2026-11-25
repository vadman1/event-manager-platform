package io.github.vadman1.eventcommon;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EventChangeKafkaMessage(
        UUID messageId,
        String eventType,
        Long eventId,
        OffsetDateTime occurredAt,
        Long ownerId,
        Long changedById,
        List<Long> subscribers,
        List<ChangeItem> changes
) {
}
