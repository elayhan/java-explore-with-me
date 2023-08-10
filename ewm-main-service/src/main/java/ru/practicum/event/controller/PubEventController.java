package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.enums.PubSort;
import ru.practicum.event.service.EventService;
import ru.practicum.utils.CustomDateFormat;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/events")
public class PubEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAllEvent(@RequestParam(required = false) String text,
                                           @RequestParam(required = false) Set<@PositiveOrZero Long> categories,
                                           @RequestParam(required = false) Boolean paid,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = CustomDateFormat.PATTERN) LocalDateTime rangeStart,
                                           @RequestParam(required = false) @DateTimeFormat(pattern = CustomDateFormat.PATTERN) LocalDateTime rangeEnd,
                                           @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(defaultValue = "EVENT_DATE") PubSort sort,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        return service.getAllEventByPub(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable @PositiveOrZero Long id) {
        return service.getEventByIdForPub(id);
    }
}
