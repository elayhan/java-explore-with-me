package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAllEvents(@PathVariable @PositiveOrZero Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return service.getEventByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @PositiveOrZero Long userId,
                                 @RequestBody @Valid NewEventDto newEventDto) {
        return service.addEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable @PositiveOrZero Long userId,
                                 @PathVariable @PositiveOrZero Long eventId) {
        return service.getEventByIdAndUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero Long userId,
                                    @PathVariable @PositiveOrZero Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return service.updateEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsByUserEvents(@PathVariable @PositiveOrZero Long userId,
                                                                              @PathVariable @PositiveOrZero Long eventId) {
        return service.getParticipationRequestsByUserEvents(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestStatus(@PathVariable @PositiveOrZero Long userId,
                                                              @PathVariable @PositiveOrZero Long eventId,
                                                              @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return service.updateRequestStatus(userId, eventId, updateRequest);
    }

}
