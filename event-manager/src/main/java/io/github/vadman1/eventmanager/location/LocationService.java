package io.github.vadman1.eventmanager.location;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter entityConverter;

    public LocationService(
            LocationRepository locationRepository,
            LocationEntityConverter entityConverter
    ) {
        this.locationRepository = locationRepository;
        this.entityConverter = entityConverter;
    }

    public Location createLocation(Location location) {
        LocationEntity locationToSave = entityConverter.toEntity(location);

        return entityConverter.toDomain(
                locationRepository.save(locationToSave)
        );
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(entityConverter::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Not found location by id=%s"
                        .formatted(id)));
    }

    public Location updateLocation(Long id, Location locationToUpdate) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found location by id=%s"
                    .formatted(id));
        }

        LocationEntity locationEntityToUpdate = entityConverter.toEntity(locationToUpdate);
        locationEntityToUpdate.setId(id);

        var updatedLocation = locationRepository.save(locationEntityToUpdate);
        return entityConverter.toDomain(updatedLocation);
    }

    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Not found location by id=%s"
                    .formatted(id));
        }

        locationRepository.deleteById(id);
    }

}
