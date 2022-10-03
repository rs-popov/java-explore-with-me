package ru.practicum.ewm.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilations.dto.CompilationInputDto;
import ru.practicum.ewm.compilations.dto.CompilationMapper;
import ru.practicum.ewm.compilations.dto.CompilationOutputDto;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.dto.EventOutputShortDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.compilations.repository.CompilationRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.requests.repository.RequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public CompilationOutputDto createCompilation(CompilationInputDto compilationInputDto) {
        Set<Event> events = compilationInputDto.getEvents().stream()
                .map(id -> eventRepository.findById(id)
                        .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + id + " was not found.")))
                .collect(Collectors.toSet());
        Compilation compilation = CompilationMapper.toCompilationFromInputDto(compilationInputDto, events);
        Set<EventOutputShortDto> eventsOutput = events.stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toSet());
        return CompilationMapper.toCompilationOutputDto(compilationRepository.save(compilation), eventsOutput);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        compilationRepository.delete(compilation);
        log.info("Compilation id={} removed", compId);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        if (compilation.getEvents().contains(event)) {
            throw new BadRequestException("Event has already been added to the compilation.");
        }
        if (compilationRepository.addEventToCompilation(compId, eventId) == 0) {
            log.info("Event id={} added to compilation id={}", eventId, compId);
        }
    }

    @Override
    @Transactional
    public void deleteEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
        if (!compilation.getEvents().contains(event)) {
            throw new BadRequestException("Event is not included in the compilation.");
        }
        if (compilationRepository.deleteEventToCompilation(compId, eventId) == 0) {
            log.info("Event id={} removed from compilation id={}", eventId, compId);
        }
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        if (compilation.getPinned()) {
            throw new BadRequestException("Compilation has already been pinned.");
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id={} was pinned", compId);
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        if (!compilation.getPinned()) {
            throw new BadRequestException("Compilation is already unpinned.");
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id={} was unpinned", compId);
    }

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.getCompilations(pinned, getPageRequest(from, size)).stream()
                .map(compilation -> CompilationMapper.toCompilationOutputDto(compilation,
                        compilation.getEvents().stream()
                                .map(this::getShortOutputDto)
                                .collect(Collectors.toSet())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationOutputDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
        Set<EventOutputShortDto> eventsOutput = compilation.getEvents().stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toSet());
        return CompilationMapper.toCompilationOutputDto(compilation, eventsOutput);
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }

    private EventOutputShortDto getShortOutputDto(Event event) {
        long requests = requestRepository.getCountConfirmedRequestByEventId(event.getId());
        long views = 0; //TODO
        return EventMapper.toEventOutputShortDto(event, requests, views);
    }
}