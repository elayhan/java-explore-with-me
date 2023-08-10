package ru.practicum.request.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenerationTime;
import ru.practicum.event.model.Event;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    RequestStatus status;

    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    @Column(insertable = false)
    LocalDateTime created;
}
