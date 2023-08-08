package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.util.PageableCreator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        return mapper.compilationToCompilationDto(repository
                .save(mapper.newCompilationDtoToCompilation(newCompilationDto)));
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!repository.existsById(compId)) {
            throw new NotFoundException("Подборка не найдена");
        }
        repository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, NewCompilationDto newCompilationDto) {
        NewCompilationDto updCompDto = mapper.compilationToNewCompilationDto(repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена")));
        if (!newCompilationDto.getEvents().isEmpty()) {
            updCompDto.setEvents(newCompilationDto.getEvents());
        }

        if (newCompilationDto.getTitle() != null) {
            updCompDto.setTitle(newCompilationDto.getTitle());
        }

        if (newCompilationDto.getPinned() != null) {
            updCompDto.setPinned(newCompilationDto.getPinned());
        }

        return mapper.compilationToCompilationDto(repository
                .save(mapper.newCompilationDtoToCompilation(updCompDto)));
    }

    @Override
    public List<CompilationDto> getAllCompilation(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageableCreator.getPageable(from, size);
        return mapper.compilationListToCompilationDtoList(repository.findAllByPinnedOrderById(pinned, pageable));
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        return mapper.compilationToCompilationDto(repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка не найдена")));
    }
}
