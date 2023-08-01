package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.EndpointHitDto;
import ru.practicum.model.EndpointHit;

@Mapper
public interface EndpointHitMapper {
    EndpointHit toEndpointHit(EndpointHitDto endpointHitDto);

}
