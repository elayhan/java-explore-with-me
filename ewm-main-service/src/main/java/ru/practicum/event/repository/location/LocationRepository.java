package ru.practicum.event.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
