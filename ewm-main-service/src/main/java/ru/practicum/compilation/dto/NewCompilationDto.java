package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.exception.ValidateException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    Long id;
    Set<Long> events = new HashSet<>();

    Boolean pinned = false;

    @NotBlank(groups = ValidateException.OnCreate.class)
    @Size(min = 1, max = 50, groups = {ValidateException.OnCreate.class, ValidateException.OnUpdate.class})
    String title;
}
