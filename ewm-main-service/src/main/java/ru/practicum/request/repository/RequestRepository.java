package ru.practicum.request.repository;

import org.mapstruct.Named;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Named("getCountConfirmedRequestByEvent")
    @Query("select count(*) from Request r " +
            "where r.status = 'CONFIRMED' " +
            "and r.event.id = :eventId ")
    Long getCountConfirmedRequestByEvent(Long eventId);

    Optional<Request> findByIdAndRequesterId(Long id, Long requesterId);

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query("update Request r set status = 'REJECTED' where r.status = 'PENDING' and r.event.id = :eventId")
    void rejectOverLimitRequestEvent(Long eventId);
}
