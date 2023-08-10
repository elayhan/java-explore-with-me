package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {EventRepository.class, EventMapper.class})
public interface CompilationMapper {
    CompilationDto compilationToCompilationDto(Compilation compilation);


    @Mapping(conditionExpression = "java(!newCompilationDto.getEvents().isEmpty())",
            target = "events",
            source = "events",
            qualifiedByName = "findEvents",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
    Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto);

    List<CompilationDto> compilationListToCompilationDtoList(List<Compilation> compilationList);

    NewCompilationDto compilationToNewCompilationDto(Compilation compilation);

    default Set<Long> eventIdToLong(Set<Event> events) {
        return events.stream().map(Event::getId).collect(Collectors.toSet());
    }
}
