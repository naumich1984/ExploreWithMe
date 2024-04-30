package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.CompilationEvent;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.EventShortFlatDto;
import ru.practicum.ewm.model.dto.NewCompilationDto;
import ru.practicum.ewm.model.mapper.CompilationMapper;
import ru.practicum.ewm.model.mapper.EventMapper;
import ru.practicum.ewm.model.request.UpdateCompilationRequest;
import ru.practicum.ewm.repository.CompilationEventRepository;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.utility.CommonPageRequest;
import ru.practicum.ewm.utility.StatsInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;
    private final EventRepository eventRepository;
    private final StatsInfo statsInfo;

    @Override
    @Transactional
    public CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto) {
        log.debug("RUN addCompilation");
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto));
        List<CompilationEvent> compilationEvents = compilationEventRepository.saveAll(CompilationMapper
                .toCompilationEvent(compilation, newCompilationDto.getEvents()));
        List<Event> events = eventRepository.findAllById(compilationEvents.stream()
                .map(compilationEvent -> compilationEvent.getEvent().getId())
                .collect(Collectors.toList()));

        return CompilationMapper.toCompilationDto(compilation, events.stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        statsInfo.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/event/" + event.getId()), true)))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteCompilationAdmin(Long compId) {
        log.debug("RUN deleteCompilation");
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilationAdmin(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        log.debug("RUN updateCompilationAdmin");
        Compilation currentCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        Compilation compilation = compilationRepository.saveAndFlush(CompilationMapper.
                toCompilationFromUpdateCompilationRequest(updateCompilationRequest, currentCompilation));

        List<Event> events = List.of();
        Optional<List<Long>> newEventIdsO = Optional.ofNullable(updateCompilationRequest.getEvents());
        if (newEventIdsO.isEmpty()) {
            //Обновление events не требуется
            ;
        } else {
            if (updateCompilationRequest.getEvents().isEmpty()) {
                //Делаем подборку пустой
                compilationEventRepository.deleteAllByCompilationId(compId);
            } else {
                List<Long> newEventIds = newEventIdsO.get();
                List<Long> currentEventIds = compilationRepository.findAllEventIdsByCompilationId(compilation.getId())
                        .orElse(List.of());

                List<Long> eventIdsToDel = currentEventIds.stream().filter(id -> !newEventIds.contains(id))
                        .collect(Collectors.toList());
                List<Long> eventIdsToAdd = newEventIds.stream().filter(id -> !currentEventIds.contains(id))
                        .collect(Collectors.toList());

                List<CompilationEvent> compilationEvents = List.of();
                if (!eventIdsToDel.isEmpty()) {
                    compilationEventRepository.deleteAllByCompilationIdAndEventIdIn(compId, eventIdsToDel);
                }
                if (!eventIdsToAdd.isEmpty()) {
                    compilationEvents = compilationEventRepository.saveAllAndFlush(CompilationMapper
                            .toCompilationEvent(compilation, eventIdsToAdd));
                }

                events = eventRepository.findAllById(compilationEvents.stream()
                        .map(compilationEvent -> compilationEvent.getEvent().getId())
                        .collect(Collectors.toList()));
            }
        }


        return CompilationMapper.toCompilationDto(compilation, events.stream()
                .map(event -> EventMapper.toEventShortDto(event,
                        statsInfo.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/event/" + event.getId()), true)))
                .collect(Collectors.toList()));
    }

    @Override
    public List<CompilationDto> getCompilationPublic(Boolean pinned, Integer from, Integer size) {
        log.debug("RUN deleteCompilation");
        CommonPageRequest commonPageRequest = new CommonPageRequest(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, commonPageRequest);

        List<CompilationDto> results = new ArrayList<>(List.of());
        for(Compilation compilation : compilations) {
            List<Long> eventIds = compilationRepository.findAllEventIdsByCompilationId(compilation.getId()).orElse(List.of());

            List<EventShortFlatDto> eventShortFlatDtos = eventRepository.findAllEventsByIds(eventIds).orElse(List.of());

            List<EventShortDto> eventShortDtos = eventShortFlatDtos.stream()
                    .map(EventMapper::toEventShortDtoFromFlat)
                    .collect(Collectors.toList());
            results.add(CompilationMapper.toCompilationDto(compilation, eventShortDtos));
        }

        return results;
    }

    @Override
    public CompilationDto getCompilationByIdPublic(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        List<Long> eventIds = compilationRepository.findAllEventIdsByCompilationId(compilation.getId())
                .orElse(List.of());

        List<EventShortFlatDto> eventShortFlatDtos = eventRepository.findAllEventsByIds(eventIds).orElse(List.of());

        List<EventShortDto> eventShortDtos = eventShortFlatDtos.stream()
                .map(EventMapper::toEventShortDtoFromFlat)
                .collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilation, eventShortDtos);
    }
}
