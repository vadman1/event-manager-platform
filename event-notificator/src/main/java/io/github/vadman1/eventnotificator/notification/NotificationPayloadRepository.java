package io.github.vadman1.eventnotificator.notification;

import io.github.vadman1.eventcommon.EventChangeKafkaMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NotificationPayloadRepository extends JpaRepository<NotificationPayloadEntity, Long> {

    Optional<NotificationPayloadEntity> findByMessageId(UUID messageId);

    @Modifying
    @Query(value = """
            insert into notification_event_payloads
                (message_id, event_type, event_id, occurred_at, owner_id, changed_by_id, payload_json)
            values (:#{#message.messageId()}, :#{#message.eventType()}, :#{#message.eventId()},
                    :#{#message.occurredAt()}, :#{#message.ownerId()}, :#{#message.changedById()}, :json)
            on conflict (message_id) do nothing
            """, nativeQuery = true)
    int insertIfAbsent(@Param("message") EventChangeKafkaMessage message,
                       @Param("json") String json);

    @Modifying
    @Query("""
            delete from NotificationPayloadEntity p
            where not exists (select 1 from NotificationEntity n where n.payload = p)
            """)
    void deleteOrphans();
}
