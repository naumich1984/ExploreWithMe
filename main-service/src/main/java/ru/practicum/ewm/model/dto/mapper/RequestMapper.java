package ru.practicum.ewm.model.dto.mapper;

import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {

        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus()).build();
    }
}
