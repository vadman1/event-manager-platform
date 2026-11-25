package io.github.vadman1.eventmanager.event;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from EventEntity e where e.id = :id")
    Optional<EventEntity> findByIdForUpdate(@Param("id") Long id);

    List<EventEntity> findAllByOwnerId(Long ownerId);

    boolean existsByLocationId(Long locationId);

    boolean existsByLocationIdAndMaxPlacesGreaterThan(Long locationId, Integer capacity);

    @Query("""
            select e from EventEntity e
            where e.name = coalesce(:#{#filter.name()}, e.name)
              and e.maxPlaces >= coalesce(:#{#filter.placesMin()}, e.maxPlaces)
              and e.maxPlaces <= coalesce(:#{#filter.placesMax()}, e.maxPlaces)
              and e.startAt >= coalesce(:#{#filter.dateStartAfter()}, e.startAt)
              and e.startAt <= coalesce(:#{#filter.dateStartBefore()}, e.startAt)
              and e.cost >= coalesce(:#{#filter.costMin()}, e.cost)
              and e.cost <= coalesce(:#{#filter.costMax()}, e.cost)
              and e.durationMinutes >= coalesce(:#{#filter.durationMin()}, e.durationMinutes)
              and e.durationMinutes <= coalesce(:#{#filter.durationMax()}, e.durationMinutes)
              and e.location.id = coalesce(:#{#filter.locationId()}, e.location.id)
              and e.status = coalesce(:#{#filter.eventStatus()}, e.status)
            """)
    List<EventEntity> search(@Param("filter") EventSearchRequestDto filter);

    @Query(value = """
            select id from event
            where (status = 'WAIT_START' and start_at <= :now)
               or (status = 'STARTED' and start_at + duration_minutes * interval '1 minute' <= :now)
            """, nativeQuery = true)
    List<Long> findDueEventIds(@Param("now") OffsetDateTime now);
}
