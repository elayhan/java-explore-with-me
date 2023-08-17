package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventIdOrderByLastDate(Long eventId, Pageable pageable);

    List<Comment> findAllByAuthorIdOrderByLastDate(Long author, Pageable pageable);
}
