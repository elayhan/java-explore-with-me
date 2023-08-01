package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> postHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("PostHit: {}", endpointHitDto.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.postHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getStats(@RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime start,
                                                       @RequestParam @DateTimeFormat(pattern = PATTERN) LocalDateTime end,
                                                       @RequestParam(required = false) List<String> uris,
                                                       @RequestParam(defaultValue = "false") boolean unique) {
        log.info("GetStats: with start = {}, end = {}, uris = {}, unique = {}", start, end, uris, unique);
        return ResponseEntity.status(HttpStatus.OK).body(service.getStats(start, end, uris, unique));
    }
}
