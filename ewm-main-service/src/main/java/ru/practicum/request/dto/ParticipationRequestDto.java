package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.request.enums.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    @JsonFormat(pattern = PATTERN)
    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    RequestStatus status;
}
