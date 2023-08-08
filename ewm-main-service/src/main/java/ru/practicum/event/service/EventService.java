package ru.practicum.event.service;

import ru.practicum.event.dto.*;
import ru.practicum.event.enums.PubSort;
import ru.practicum.event.model.state.EventState;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> getEventByUser(Long userId, int from, int size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventByIdAndUser(Long userId, Long eventId);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<EventFullDto> getAllEventForAdmin(Set<Long> users,
                                           Set<EventState> states,
                                           Set<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from, int size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAllEventByPub(String text,
                                         Set<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Boolean onlyAvailable,
                                         PubSort sort,
                                         int from,
                                         int size);

    EventFullDto getEventByIdForPub(Long id);

    List<ParticipationRequestDto> getParticipationRequestsByUserEvents(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);
}
