package ru.practicum.event.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.enums.EventRequestState.UserRequestState;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest extends UpdateEventDto {
    UserRequestState stateAction;
}
