package io.github.vadman1.eventmanager.location;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from LocationEntity l where l.id = :id")
    Optional<LocationEntity> findByIdForUpdate(@Param("id") Long id);
}
