package ru.practicum.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.event.enums.PubSort;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableCreator {
    public static Pageable getPageable(@Valid @Min(value = 0, message = "Не может быть меньше 0") Integer from,
                                       @Valid @Min(value = 1, message = "Больше нуля") Integer size) {
        return PageRequest.of(from == 0 ? 0 : from / size, size);
    }

    public static Pageable getPageable(@Valid @Min(value = 0, message = "Не может быть меньше 0") Integer from,
                                       @Valid @Min(value = 1, message = "Больше нуля") Integer size,
                                       PubSort sort) {
        if (sort.equals(PubSort.EVENT_DATE)) {
            return PageRequest.of(from == 0 ? 0 : from / size, size, Sort.by("eventDate").descending());
        }
        return getPageable(from, size);
    }
}
