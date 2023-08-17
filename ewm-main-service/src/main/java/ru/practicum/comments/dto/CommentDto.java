package ru.practicum.comments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;
    String userName;
    String titleEvent;
    String comment;
    Boolean isModified;
    @JsonFormat(pattern = PATTERN)
    LocalDateTime lastDate;
}
