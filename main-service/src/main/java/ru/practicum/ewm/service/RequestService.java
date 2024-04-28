package ru.practicum.ewm.service;

import ru.practicum.ewm.model.dto.ParticipationRequestDto;

import java.util.List;


public interface RequestService {

    List<ParticipationRequestDto> getRequestsPrivate(Long userId);

    ParticipationRequestDto addRequestPrivate(Long userId, Long eventId);
}
