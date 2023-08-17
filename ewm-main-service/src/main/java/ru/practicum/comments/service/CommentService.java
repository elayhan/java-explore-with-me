package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto);

    CommentDto updateCommentByAdmin(Long commentId, NewCommentDto newCommentDto);

    CommentDto getCommentById(Long commentId);

    void deleteCommentByUser(Long userId, Long eventId, Long commendId);

    void deleteCommentById(Long commentId);

    List<CommentDto> getAllCommentsOnEvent(Long eventId, int from, int size);

    List<CommentDto> getAllUserComments(Long userId, int from, int size);
}
