package ru.practicum;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StatClient {
    ResponseEntity<List<ViewStatsDto>> getStat(String start,
                                               String end,
                                               List<String> uris,
                                               boolean unique);

    ResponseEntity<EndpointHitDto> hitStat(EndpointHitDto endpointHitDto);
}
