package ru.practicum.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserDto {
    Long id;
    @NotBlank(message = "Поле name не должно быть пустым")
    @Size(min = 2, max = 250, message = "Имя может быть от 2 до 250 символов")
    String name;

    @NotBlank(message = "Поле email не должно быть пустым")
    @Size(min = 6, max = 254, message = "Email от 6 до 254 символов")
    @Email(message = "Email не корректный")
    String email;
}
