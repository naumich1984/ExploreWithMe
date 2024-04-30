package ru.practicum.ewm.service;

import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.NewCompilationDto;
import ru.practicum.ewm.model.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilationAdmin(NewCompilationDto newCompilationDto);

    CompilationDto updateCompilationAdmin(UpdateCompilationRequest updateCompilationRequest, Long compId);

    void deleteCompilationAdmin(Long compId);

    List<CompilationDto> getCompilationPublic(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationByIdPublic(Long compId);
}
