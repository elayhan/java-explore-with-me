package ru.practicum.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiError {
    List<String> errors;
    String message;
    String reason;
    HttpStatus status;
    @Builder.Default
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(PATTERN));
}
