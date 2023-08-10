package ru.practicum.event.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.enums.EventRequestState.AdminRequestState;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventAdminRequest extends UpdateEventDto {
    @Enumerated(EnumType.STRING)
    AdminRequestState stateAction;
}
