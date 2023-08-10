package ru.practicum.category.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class CategoryDto {
    Long id;
    @NotBlank
    @Size(min = 1, max = 50, message = "Название категории может быть от 1 до 50 символов")
    String name;
}
