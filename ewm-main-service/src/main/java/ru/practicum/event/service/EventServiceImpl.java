package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.PubSort;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.state.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.DateStartException;
import ru.practicum.exception.EventStateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestValidationException;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequest.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.mapper.RequestMapper;
import ru.practicum.request.model.Request;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.util.StatService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ru.practicum.util.PageableCreator.getPageable;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final EventMapper mapper;
    private final RequestMapper requestMapper;
    private final StatService statService;

    @Override
    public List<EventShortDto> getEventByUser(Long userId, int from, int size) {
        Pageable pageable = getPageable(from, size);
        return mapper.eventListToEventShortDto(repository.findAllByInitiatorId(userId, pageable));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория не найдена"));
    }

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Category category = getCategory(newEventDto.getCategory());
        Event event = mapper.newEventDtoToEvent(user, category, newEventDto);
        return mapper.eventToEventFullDto(repository.save(event));
    }

    @Override
    public EventFullDto getEventByIdAndUser(Long userId, Long eventId) {
        Event event = getEventByInitiatorIdAndEventId(userId, eventId);
        return mapper.eventToEventFullDto(event);
    }

    private Event getEventByInitiatorIdAndEventId(Long userId, Long eventId) {
        return repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));
    }

    private void updateEventState(Event event, String state) {
        Map<String, Consumer<String>> strategyMap = Map.of(
                "SEND_TO_REVIEW", (value) -> event.setState(EventState.PENDING),
                "CANCEL_REVIEW", (value) -> event.setState(EventState.CANCELED),
                "PUBLISH_EVENT", (value) -> {
                    if (!event.getState().equals(EventState.PENDING)) {
                        throw new EventStateException("Событие нельзя опубликовать, если изначальный статус не Ожидание публикации");
                    }
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                },
                "REJECT_EVENT", (value) -> {
                    if (event.getState().equals(EventState.PUBLISHED)) {
                        throw new EventStateException("Нельзя отклонить уже опубликованное событие");
                    }
                    event.setState(EventState.CANCELED);
                }
        );
        strategyMap.get(state).accept(state);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        getUser(userId);

        Event event = repository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventStateException("Опубликованное событие нельзя редактировать.");
        }

        mapper.updateEvent(event, updateEventUserRequest);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventStateException("До начала события слишком МАЛА времени. " +
                    "Для редактирования до события должно оставаться более 2 часов");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            updateEventState(event, updateEventUserRequest.getStateAction().toString());
        }

        return mapper.eventToEventFullDto(repository.save(event));
    }

    @Override
    public List<EventFullDto> getAllEventForAdmin(Set<Long> users,
                                                  Set<EventState> states,
                                                  Set<Long> categories,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  int from, int size) {
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(30);
        }

        if (rangeEnd.isBefore(rangeStart)) {
            throw new DateStartException("Начало позже конца");
        }

        Pageable pageable = getPageable(from, size);
        return mapper.eventListToEventFullDto(repository.getAllEventForAdmin(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                pageable));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));

        mapper.updateEvent(event, updateEventAdminRequest);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EventStateException("До начала события слишком МАЛА времени. " +
                    "Для редактирования до события должно оставаться более 1 часа");
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            updateEventState(event, updateEventAdminRequest.getStateAction().name());
        }

        return mapper.eventToEventFullDto(repository.save(event));
    }

    @Override
    public List<EventShortDto> getAllEventByPub(String text,
                                                Set<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Boolean onlyAvailable,
                                                PubSort sort,
                                                int from, int size) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(30);
        }

        if (rangeEnd.isBefore(rangeStart)) {
            throw new DateStartException("Начало позже конца");
        }

        statService.hitEvent(null);
        Pageable pageable = getPageable(from, size, sort);

        List<Event> eventList = repository.getAllEventForPub(
                text != null ? text.toLowerCase() : null,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                pageable);

        List<EventShortDto> resultEventList = mapper.eventListToEventShortDto(eventList);

        if (onlyAvailable) {
            List<Event> onlyAvailableEventList = eventList.stream().filter(event ->
                            event.getParticipantLimit() == 0 || event.getParticipantLimit() < requestRepository.getCountConfirmedRequestByEvent(event.getId())
                    )
                    .collect(Collectors.toList());
            resultEventList = mapper.eventListToEventShortDto(onlyAvailableEventList);
        }

        if (sort.equals(PubSort.VIEWS)) {
            resultEventList.sort((o1, o2) -> Math.toIntExact(o2.getViews() - o1.getViews()));
        }
        return resultEventList;
    }

    @Override
    public EventFullDto getEventByIdForPub(Long id) {
        statService.hitEvent(id);
        return mapper.eventToEventFullDto(repository.getEventByIdForPub(id)
                .orElseThrow(() -> new NotFoundException("Событие не найдено.")));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByUserEvents(Long userId, Long eventId) {
        getEventByIdAndUser(userId, eventId);
        return requestMapper.requestListToParticipationRequestDtoList(requestRepository.findAllByEventId(eventId));
    }

    private boolean is_limit_exceed(int limit, Long eventId) {
        if (limit <= requestRepository.getCountConfirmedRequestByEvent(eventId)) {
            requestRepository.rejectOverLimitRequestEvent(eventId);
            return true;
        }
        return false;
    }

    private ParticipationRequestDto updRequest(Long requestId, Long eventId, int limit, RequestStatus status) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));
        if (!request.getStatus().equals(RequestStatus.PENDING)) {
            throw new RequestValidationException("Можно подтвердить только заявки в состоянии ожидания");
        }

        request.setStatus(status);

        if (is_limit_exceed(limit, eventId)) {
            request.setStatus(RequestStatus.REJECTED);
        }

        request = requestRepository.save(request);

        return requestMapper.requestToParticipationRequestDto(request);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest) {
        Event event = getEventByInitiatorIdAndEventId(userId, eventId);
        int participantLimit = event.getParticipantLimit();

        RequestStatus status = updateRequest.getStatus();

        if (participantLimit != 0 && is_limit_exceed(participantLimit, eventId)) {
            throw new RequestValidationException("Достигнут лимит по заявкам на данное событие");
        }

        if (!event.getRequestModeration()) {
            throw new RequestValidationException("Для данного события подтверждение заявок не требуется");
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        updateRequest.getRequestIds().forEach(aLong -> {
            ParticipationRequestDto resUpdRequest = updRequest(aLong, eventId, participantLimit, status);
            assert resUpdRequest != null;
            if (resUpdRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
                confirmedRequests.add(resUpdRequest);
            } else if (resUpdRequest.getStatus().equals(RequestStatus.REJECTED)) {
                rejectedRequests.add(resUpdRequest);
            }
        });

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
