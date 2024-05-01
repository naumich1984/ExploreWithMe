package ru.practicum.ewm.model.mapper;

import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.CompilationEvent;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewCompilationDto;
import ru.practicum.ewm.model.request.UpdateCompilationRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {

        return Compilation.builder().title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned()).build();
    }

    public static List<CompilationEvent> toCompilationEvent(Compilation compilation, List<Long> eventIds) {

        return eventIds.stream().map(eventId -> CompilationEvent.builder()
                .compilation(compilation)
                .event(Event.builder().id(eventId).build()).build()).collect(Collectors.toList());

    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {

        return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), events);
    }

    public static Compilation toCompilationFromUpdateCompilationRequest(UpdateCompilationRequest updateCompilationRequest,
                                                                        Compilation compilation) {

        return Compilation.builder()
                .id(compilation.getId())
                .title(Optional.ofNullable(updateCompilationRequest.getTitle()).orElse(compilation.getTitle()))
                .pinned(Optional.ofNullable(updateCompilationRequest.getPinned()).orElse(compilation.getPinned()))
                .build();
    }
}
