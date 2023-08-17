package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.PageableCreator;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentsRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper mapper;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        return mapper.commentToCommentDto(repository
                .save(mapper.newCommentDtoToComment(user, event, newCommentDto)));
    }

    @Override
    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = checkUserEventComment(userId, eventId, commentId);

        comment.setComment(newCommentDto.getComment());
        comment.setIsModified(true);
        comment.setLastDate(LocalDateTime.now());

        return mapper.commentToCommentDto(repository.save(comment));
    }

    @Override
    public CommentDto updateCommentByAdmin(Long commentId, NewCommentDto newCommentDto) {
        Comment comment = getComment(commentId);
        comment.setComment(newCommentDto.getComment());
        return mapper.commentToCommentDto(repository.save(comment));
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        return mapper.commentToCommentDto(repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий не найден")));
    }

    @Override
    public void deleteCommentByUser(Long userId, Long eventId, Long commendId) {
        checkUserEventComment(userId, eventId, commendId);
        repository.deleteById(commendId);
    }

    private Comment checkUserEventComment(Long userId, Long eventId, Long commendId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        Comment comment = getComment(commendId);
        if (!comment.getAuthor().equals(user) || !comment.getEvent().equals(event)) {
            throw new RequestValidationException("Нельзя изменять чужие комментарии");
        }
        return comment;
    }

    @Override
    public void deleteCommentById(Long commentId) {
        getComment(commentId);
        repository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllCommentsOnEvent(Long eventId, int from, int size) {
        Pageable pageable = PageableCreator.getPageable(from, size);
        return mapper.listCommentToListCommentDto(repository.findAllByEventIdOrderByLastDate(eventId, pageable));
    }

    @Override
    public List<CommentDto> getAllUserComments(Long userId, int from, int size) {
        Pageable pageable = PageableCreator.getPageable(from, size);
        return mapper.listCommentToListCommentDto(repository.findAllByAuthorIdOrderByLastDate(userId, pageable));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено"));
    }

    private Comment getComment(Long commentId) {
        return repository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не найден"));
    }
}
