package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.exception.ValidateException;

import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Validated(ValidateException.OnCreate.class) NewCompilationDto newCompilationDto) {
        return service.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @PositiveOrZero Long compId) {
        service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable @PositiveOrZero Long compId,
                                            @RequestBody @Validated(ValidateException.OnUpdate.class) NewCompilationDto newCompilationDto) {
        return service.updateCompilation(compId, newCompilationDto);
    }
}
