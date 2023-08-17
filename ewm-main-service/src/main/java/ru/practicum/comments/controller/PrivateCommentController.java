package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateCommentController {
    private final CommentService service;

    @GetMapping("/comments")
    public List<CommentDto> getAllUserComments(@PathVariable @PositiveOrZero Long userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return service.getAllUserComments(userId, from, size);
    }

    @PostMapping("/event/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable @PositiveOrZero Long userId,
                                 @PathVariable @PositiveOrZero Long eventId,
                                 @Valid @RequestBody NewCommentDto newCommentDto) {
        return service.addComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/event/{eventId}/comments")
    public CommentDto updateComment(@PathVariable @PositiveOrZero Long userId,
                                    @PathVariable @PositiveOrZero Long eventId,
                                    @RequestParam @PositiveOrZero Long commentId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        return service.updateComment(userId, eventId, commentId, newCommentDto);
    }

    @DeleteMapping("/event/{eventId}/comments")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @PositiveOrZero Long userId,
                              @PathVariable @PositiveOrZero Long eventId,
                              @RequestParam @PositiveOrZero Long commentId) {
        service.deleteCommentByUser(userId, eventId, commentId);
    }

}
