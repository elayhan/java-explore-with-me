package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Краткое описание события от 20 до 2000 символов")
    String annotation;

    @PositiveOrZero
    Long category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Полное описание события от 20 до 7000 символов")
    String description;

    @JsonFormat(pattern = PATTERN)
    @Future
    LocalDateTime eventDate;

    @NotNull
    LocationDto location;

    Boolean paid = false;

    @PositiveOrZero
    Integer participantLimit = 0;

    Boolean requestModeration = true;

    @Size(min = 3, max = 120, message = "Заголовок события от 3 до 120 символов")
    String title;
}
