package ru.practicum.comments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {
    private final CommentService service;

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable @PositiveOrZero Long commentId) {
        return service.getCommentById(commentId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable @PositiveOrZero Long commentId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        return service.updateCommentByAdmin(commentId, newCommentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @PositiveOrZero Long commentId) {
        service.deleteCommentById(commentId);
    }

}
