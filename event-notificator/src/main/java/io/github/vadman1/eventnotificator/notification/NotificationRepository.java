package io.github.vadman1.eventnotificator.notification;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @EntityGraph(attributePaths = "payload")
    List<NotificationEntity> findAllByUserIdAndIsReadFalseOrderByCreatedAtDescIdDesc(Long userId);

    @Modifying
    @Query("""
            update NotificationEntity n set n.isRead = true, n.readAt = :now
            where n.userId = :userId and n.id in :ids and n.isRead = false
            """)
    void markAsRead(@Param("userId") Long userId, @Param("ids") List<Long> ids,
                    @Param("now") OffsetDateTime now);

    @Modifying
    @Query("delete from NotificationEntity n where n.createdAt < :cutoff")
    void deleteCreatedBefore(@Param("cutoff") OffsetDateTime cutoff);
}
