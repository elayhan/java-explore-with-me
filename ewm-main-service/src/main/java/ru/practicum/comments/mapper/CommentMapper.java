package ru.practicum.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.NewCommentDto;
import ru.practicum.comments.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Mapping(target = "userName", source = "author.name")
    @Mapping(target = "titleEvent", source = "event.title")
    CommentDto commentToCommentDto(Comment comment);

    List<CommentDto> listCommentToListCommentDto(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    Comment newCommentDtoToComment(User author, Event event, NewCommentDto newCommentDto);
}
