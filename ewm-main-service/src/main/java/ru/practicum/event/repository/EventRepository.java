package ru.practicum.event.repository;

import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.state.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByInitiatorIdAndId(Long userId, Long id);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between :rangeStart and :rangeEnd " +
            "and e.initiator.id in :users or :users is null " +
            "and e.state in :states or :states is null " +
            "and e.category.id in :categories or :categories is null ")
    List<Event> getAllEventForAdmin(Set<Long> users,
                                    Set<EventState> states,
                                    Set<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where state = 'PUBLISHED' " +
            "and e.eventDate between :rangeStart and :rangeEnd " +
            "and (e.paid = :paid or :paid is null) " +
            "and (e.category.id in :categories or :categories is null) " +
            "and (:text is null " +
            " or lower(e.annotation) like lower(CONCAT('%',:text,'%')) " +
            " or lower(e.description) like lower(CONCAT('%',:text,'%'))) ")
    List<Event> getAllEventForPub(String text,
                                  Set<Long> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Pageable pageable);

    @Query("select e from Event e " +
            "where state = 'PUBLISHED' " +
            "and e.id = :id ")
    Optional<Event> getEventByIdForPub(Long id);

    @Named("findEvents")
    Set<Event> findAllByIdIn(Set<Long> ids);

}
