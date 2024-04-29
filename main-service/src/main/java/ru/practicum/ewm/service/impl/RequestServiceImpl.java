package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model._enum.RequestStatus;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.mapper.RequestMapper;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getRequestsPrivate(Long userId) {
        log.debug("RUN getRequestsPrivate");
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        return requestRepository.findAllRequestByUserId(userId);
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequestPrivate(Long userId, Long eventId) {
        log.debug("RUN addRequestPrivate");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Conflict");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Conflict");
        }
        Long currentLimit = requestRepository.findCountConfirmedRequestByEventId(eventId);
        if (currentLimit.equals((long) event.getParticipantLimit()) && event.getParticipantLimit() > 0) {
            throw new ValidationException("Conflict");
        }
        if (requestRepository.findByEventIdAndUserId(eventId, userId).isPresent()) {
            throw new ValidationException("Conflict");
        }

        RequestStatus requestStatus = RequestStatus.PENDING;
        if (!event.getRequestModeration() || event.getParticipantLimit().equals(0) ) {
            requestStatus = RequestStatus.CONFIRMED;
        }
        Request result = requestRepository.save(Request.builder()
                .requester(user)
                .event(event)
                .status(requestStatus)
                .build());

        return RequestMapper.toParticipationRequestDto(result);
    }

    @Override
    public ParticipationRequestDto cancelRequestPrivate(Long userId, Long requestId) {
        log.debug("RUN cancelRequestPrivate");
        Request request = requestRepository.findByRequestIdAndUserId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));

        request.setStatus(RequestStatus.CANCELED);
        requestRepository.save(request);

        return RequestMapper.toParticipationRequestDto(request);
    }
}
