package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class PubCommentController {
    private final CommentService service;

    @GetMapping
    public List<CommentDto> getAllCommentsOnEvent(@PathVariable @PositiveOrZero Long eventId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return service.getAllCommentsOnEvent(eventId, from, size);
    }
}
