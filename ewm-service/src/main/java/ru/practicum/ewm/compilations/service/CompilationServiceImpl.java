package ru.practicum.ewm.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilations.model.Compilation;
import ru.practicum.ewm.compilations.model.dto.CompilationInputDto;
import ru.practicum.ewm.compilations.model.dto.CompilationMapper;
import ru.practicum.ewm.compilations.model.dto.CompilationOutputDto;
import ru.practicum.ewm.compilations.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.EventMapper;
import ru.practicum.ewm.event.model.dto.EventOutputShortDto;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.logging.CreationLogging;
import ru.practicum.ewm.logging.DeletionLogging;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.statistics.client.StatisticsClient;

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
    private final StatisticsClient statisticsClient;

    @Override
    @Transactional
    @CreationLogging
    public CompilationOutputDto createCompilation(CompilationInputDto compilationInputDto) {
        Set<Event> events = compilationInputDto.getEvents().stream()
                .map(this::getEvent)
                .collect(Collectors.toSet());
        Compilation compilation = CompilationMapper.toCompilationFromInputDto(compilationInputDto, events);
        return CompilationMapper.toCompilationOutputDto(compilationRepository.save(compilation), getEventsOutputByCompilation(compilation));
    }

    @Override
    @Transactional
    @DeletionLogging
    public void deleteCompilation(Long compId) {
        Compilation compilation = getCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilation(compId);
        Event event = getEvent(eventId);
        if (compilation.getEvents().contains(event)) {
            log.warn("Event was not added to compilation. Event has already been added to the compilation.");
            throw new BadRequestException("Event has already been added to the compilation.");
        }
        if (compilationRepository.addEventToCompilation(compId, eventId) == 1) {
            log.info("Event id={} added to compilation id={}", eventId, compId);
        }
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = getCompilation(compId);
        Event event = getEvent(eventId);
        if (!compilation.getEvents().contains(event)) {
            log.warn("Event was not remove from compilation. Event is not included in the compilation.");
            throw new BadRequestException("Event is not included in the compilation.");
        }
        if (compilationRepository.deleteEventToCompilation(compId, eventId) == 1) {
            log.info("Event id={} removed from compilation id={}", eventId, compId);
        }
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        Compilation compilation = getCompilation(compId);
        if (compilation.getPinned()) {
            log.warn("Compilation was not pinned because it has already been pinned.");
            throw new BadRequestException("Compilation has already been pinned.");
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id={} was pinned", compId);
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        Compilation compilation = getCompilation(compId);
        if (!compilation.getPinned()) {
            log.warn("Compilation was not unpinned because it has already been unpinned.");
            throw new BadRequestException("Compilation is already unpinned.");
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id={} was unpinned", compId);
    }

    @Override
    public List<CompilationOutputDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.getCompilations(pinned, getPageRequest(from, size)).stream()
                .map(compilation -> CompilationMapper.toCompilationOutputDto(compilation, getEventsOutputByCompilation(compilation)))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationOutputDto getCompilationById(Long compId) {
        Compilation compilation = getCompilation(compId);
        return CompilationMapper.toCompilationOutputDto(compilation, getEventsOutputByCompilation(compilation));
    }

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Compilation with id=" + compId + " was not found."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
    }

    private PageRequest getPageRequest(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        return PageRequest.of(page, size);
    }

    private EventOutputShortDto getShortOutputDto(Event event) {
        long requests = requestRepository.getCountConfirmedRequestByEventId(event.getId());
        long views = statisticsClient.getStatsForEvent(event.getId());
        return EventMapper.toEventOutputShortDto(event, requests, views);
    }

    private Set<EventOutputShortDto> getEventsOutputByCompilation(Compilation compilation) {
        return compilation.getEvents().stream()
                .map(this::getShortOutputDto)
                .collect(Collectors.toSet());
    }
}