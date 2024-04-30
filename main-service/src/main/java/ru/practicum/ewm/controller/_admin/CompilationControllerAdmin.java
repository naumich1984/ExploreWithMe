package ru.practicum.ewm.controller._admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.NewCompilationDto;
import ru.practicum.ewm.model.request.UpdateCompilationRequest;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationDto> addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.debug("POST /admin/compilations");
        log.debug(" | events: {}", newCompilationDto.getEvents());
        log.debug(" | title: {}", newCompilationDto.getTitle());
        log.debug(" | pinned: {}", newCompilationDto.getPinned());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(compilationService.addCompilationAdmin(newCompilationDto));
    }

    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@Valid @RequestBody UpdateCompilationRequest updateCompilationRequest,
                                                            @PathVariable Long compId) {
        log.debug("PATCH /admin/compilations/{compId}");
        log.debug(" | compId: {}", compId);
        log.debug(" | events: {}", updateCompilationRequest.getEvents());
        log.debug(" | pinned: {}", updateCompilationRequest.getPinned());
        log.debug(" | title: {}", updateCompilationRequest.getTitle());

        return ResponseEntity.status(HttpStatus.OK)
                .body(compilationService.updateCompilationAdmin(updateCompilationRequest, compId));
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @NotNull Long compId) {
        log.debug("DELETE /admin/compilations/{compId}");
        log.debug(" | compId: {}", compId);

        compilationService.deleteCompilationAdmin(compId);
    }
}
