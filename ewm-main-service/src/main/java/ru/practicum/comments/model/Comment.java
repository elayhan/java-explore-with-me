package ru.practicum.comments.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User author;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;

    String comment;

    @Column(name = "is_modified", insertable = false)
    Boolean isModified = false;

    @Column(name = "lastdate", insertable = false)
    LocalDateTime lastDate = LocalDateTime.now();
}
