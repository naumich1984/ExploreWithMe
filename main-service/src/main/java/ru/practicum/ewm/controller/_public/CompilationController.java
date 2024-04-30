package ru.practicum.ewm.controller._public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.model.dto.NewCompilationDto;
import ru.practicum.ewm.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDto>> getCompilationPublic(@RequestParam(defaultValue = "false") Boolean pinned,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("GET /compilations");
        log.debug(" | pinned: {}", pinned);
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(compilationService.getCompilationPublic(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationDto> getCompilationByIdPublic(@PathVariable Long compId) {
        log.debug("GET /compilations/{compId}");
        log.debug(" | compId: {}", compId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(compilationService.getCompilationByIdPublic(compId));
    }

}
