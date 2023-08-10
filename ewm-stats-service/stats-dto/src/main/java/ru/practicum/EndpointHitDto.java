package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.utils.IpV4Format;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static ru.practicum.utils.CustomDateFormat.PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {
    @NotBlank
    String app;
    @NotBlank
    String uri;
    @NotBlank
    @Pattern(regexp = IpV4Format.PATTERN)
    String ip;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN)
    @Builder.Default()
    LocalDateTime timestamp = LocalDateTime.now();
}