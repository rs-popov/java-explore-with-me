package ru.practicum.ewm.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.BadRequestException;
import ru.practicum.ewm.exceptions.ForbiddenAccessException;
import ru.practicum.ewm.exceptions.ObjectNotFoundException;
import ru.practicum.ewm.logging.CreationLogging;
import ru.practicum.ewm.requests.model.Request;
import ru.practicum.ewm.requests.model.RequestStatus;
import ru.practicum.ewm.requests.model.dto.RequestDto;
import ru.practicum.ewm.requests.model.dto.RequestMapper;
import ru.practicum.ewm.requests.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getAllRequestByRequester(Long userId) {
        return requestRepository.findRequestsByUserId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CreationLogging
    public RequestDto createRequest(Long userId, Long eventId) {
        if (requestRepository.findRequestByUserIdAndEventId(userId, eventId).isPresent()) {
            log.warn("The request already has been created. Can not create a repeat request.");
            throw new BadRequestException("Can not create a repeat request.");
        }
        Event event = getEvent(eventId);
        User requester = getUser(userId);
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            log.warn("Request was not create. Initiator cannot add a request to participate in own event.");
            throw new BadRequestException("Initiator cannot add a request to participate in own event.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.warn("Request was not create due to Event is not published.");
            throw new BadRequestException("Event is not published.");
        }
        if (event.getParticipantLimit() != 0
                && event.getParticipantLimit() <= requestRepository.getCountConfirmedRequestByEventId(eventId)) {
            log.warn("Request was not create due to participant limit is over.");
            throw new ForbiddenAccessException("Limit of requests for participation has been exceeded.");
        }
        Request request = Request.builder()
                .event(event)
                .user(requester)
                .build();
        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDto cancelRequestByRequester(Long userId, Long requestId) {
        getUser(userId);
        Request request = getRequest(requestId);
        if (Objects.equals(request.getStatus(), RequestStatus.CANCELED)) {
            log.warn("Request was not cancel due it already in cancel status.");
            throw new BadRequestException("Request with id=" + requestId + " already was canceled.");
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public List<RequestDto> getRequestsByInitiator(Long userId, Long eventId) {
        Event event = getEvent(eventId);
        User initiator = getUser(userId);
        if (!Objects.equals(event.getInitiator().getId(), initiator.getId())) {
            throw new ForbiddenAccessException("Only initiator can get all requests to participate in event id=" + eventId + ".");
        }
        return requestRepository.findRequestByEventId(eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmRequestByInitiator(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);
        User initiator = getUser(userId);
        if (!Objects.equals(event.getInitiator().getId(), initiator.getId())) {
            throw new ForbiddenAccessException("Only initiator can confirm request to participate in event id=" + eventId + ".");
        }
        Request request = getRequest(reqId);
        if (request.getStatus() == RequestStatus.CONFIRMED) {
            log.warn("Request was not confirmed due it already approve.");
            throw new BadRequestException("Request with id=" + reqId + " already was confirmed.");
        }
        request.setStatus(RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId) {
        Event event = getEvent(eventId);
        User initiator = getUser(userId);
        if (!Objects.equals(event.getInitiator().getId(), initiator.getId())) {
            throw new ForbiddenAccessException("Only initiator can reject request to participate in event id=" + eventId + ".");
        }
        Request request = getRequest(reqId);
        if (request.getStatus() == RequestStatus.REJECTED) {
            log.warn("Request was not rejected due it already in reject status.");
            throw new BadRequestException("Request with id=" + reqId + " already was rejected.");
        }
        request.setStatus(RequestStatus.REJECTED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event with id=" + eventId + " was not found."));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id=" + userId + " was not found."));
    }

    private Request getRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Request with id=" + requestId + " was not found."));
    }
}