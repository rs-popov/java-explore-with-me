package ru.practicum.ewm.compilations.service;

import ru.practicum.ewm.compilations.dto.CompilationInputDto;
import ru.practicum.ewm.compilations.dto.CompilationOutputDto;

import java.util.List;

public interface CompilationService {
    CompilationOutputDto createCompilation(CompilationInputDto compilation);

    void deleteCompilation(Long compId);

    void addEventToCompilation(Long compId, Long eventId);

    void deleteEventToCompilation(Long compId, Long eventId);

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

    List<CompilationOutputDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationOutputDto getCompilationById(Long compId);
}