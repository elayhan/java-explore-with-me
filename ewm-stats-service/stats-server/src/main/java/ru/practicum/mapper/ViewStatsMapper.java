package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.ViewStats;

import java.util.List;

@Mapper
public interface ViewStatsMapper {
    List<ViewStatsDto> toListDto(List<ViewStats> viewStatsList);
}
