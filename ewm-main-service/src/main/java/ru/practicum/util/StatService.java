package ru.practicum.util;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHitDto;
import ru.practicum.StatClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.exception.GetStatsException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatClient statClient;
    private static final String APP = "ewm-main-service";
    private static final String URI_PREFIX = "/events";

    public void hitEvent(Long eventId) {
        String uri = URI_PREFIX + (eventId == null ? "" : "/" + eventId);
        statClient.hitStat(EndpointHitDto.builder()
                .app(APP)
                .ip("127.0.0.1")
                .uri(uri)
                .build());
    }

    @Named("getViews")
    public Long getView(Long eventId) {
        List<String> uris = List.of("/events/" + eventId);
        ResponseEntity<List<ViewStatsDto>> entity = statClient.getStat(
                LocalDateTime.of(2000, 1, 1, 1, 1).format(DateTimeFormatter.ofPattern(PATTERN)),
                LocalDateTime.of(4000, 1, 1, 1, 1).format(DateTimeFormatter.ofPattern(PATTERN)),
                uris,
                true);

        if (entity.getStatusCode() != HttpStatus.OK) {
            throw new GetStatsException("получить статистику не удалось");
        }

        if (entity.getBody().isEmpty()) {
            return 0L;
        }
        return entity.getBody().stream().mapToLong(ViewStatsDto::getHits).sum();

    }

}
