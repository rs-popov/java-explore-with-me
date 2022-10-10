package ru.practicum.ewm.compilations.model.dto;

import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;


public class CompilationMapper {
    public static Compilation toCompilationFromInputDto(CompilationInputDto compilationInputDto, Set<Event> events) {
        return Compilation.builder()
                .pinned(compilationInputDto.getPinned())
                .title(compilationInputDto.getTitle())
                .events(events)
                .build();
    }

    public static CompilationOutputDto toCompilationOutputDto(Compilation compilation, Set<EventOutputShortDto> events) {
        return CompilationOutputDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}
