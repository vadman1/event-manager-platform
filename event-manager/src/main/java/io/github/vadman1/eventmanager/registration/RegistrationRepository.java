package io.github.vadman1.eventmanager.registration;

import io.github.vadman1.eventmanager.event.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByEventIdAndUserId(Long eventId, Long userId);

    @Query("select r.event from RegistrationEntity r where r.user.id = :userId")
    List<EventEntity> findEventsByUserId(@Param("userId") Long userId);
}
