package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.state.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestValidationException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper mapper;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        Request request = repository.saveAndFlush(validation(userId, eventId));
        return mapper.requestToParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return mapper.requestListToParticipationRequestDtoList(repository.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto canselRequest(Long userId, Long requestId) {
        Request request = repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Заявка для отмены не найдена"));
        request.setStatus(RequestStatus.CANCELED);
        return mapper.requestToParticipationRequestDto(repository.save(request));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow((() -> new NotFoundException("Указанное в запросе Событие не найдено")));
    }

    private Request validation(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (event.getInitiator().equals(user)) {
            throw new RequestValidationException("Инициатор события не может добавить запрос на участие в своём событии");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestValidationException("нельзя участвовать в неопубликованном событии");
        }

        Long confirmedRequest = repository.getCountConfirmedRequestByEvent(eventId);

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedRequest) {
            throw new RequestValidationException("У события достигнут лимит запросов на участие");
        }

        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            return Request.builder()
                    .requester(user)
                    .event(event)
                    .status(RequestStatus.CONFIRMED)
                    .build();
        }

        return Request.builder()
                .requester(user)
                .event(event)
                .status(RequestStatus.PENDING)
                .build();
    }
}
