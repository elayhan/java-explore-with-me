package ru.practicum.event.mapper;

import org.mapstruct.*;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.dto.location.LocationDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.location.Location;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.util.StatService;

import java.util.List;

@Mapper(uses = {CategoryMapper.class, UserMapper.class, StatService.class, RequestRepository.class, CategoryService.class})
public interface EventMapper {
    @Mapping(target = "initiator", source = "user")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "id", ignore = true)
    Event newEventDtoToEvent(User user, Category category, NewEventDto newEventDto);

    @Mapping(target = "id", source = "event.id")
    @Mapping(target = "views", source = "id", qualifiedByName = "getViews")
    @Mapping(target = "confirmedRequests", source = "id", qualifiedByName = "getCountConfirmedRequestByEvent")
    EventShortDto eventToEventShortDto(Event event);

    @Mapping(target = "id", expression = "java(event.getId())")
    @Mapping(target = "views", source = "id", qualifiedByName = "getViews")
    @Mapping(target = "confirmedRequests", source = "id", qualifiedByName = "getCountConfirmedRequestByEvent")
    EventFullDto eventToEventFullDto(Event event);

    Location locationDtoToLocation(LocationDto locationDto);

    List<EventShortDto> eventListToEventShortDto(List<Event> eventList);

    List<EventFullDto> eventListToEventFullDto(List<Event> eventList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateEvent(@MappingTarget Event event, UpdateEventDto updateEventDto);

}
