package ru.practicum.ewm.controller._public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.model.dto.CompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationDto>> getCompilationPublic(@RequestParam(defaultValue = "false") Boolean pinned,
                                                                     @RequestParam(defaultValue = "0", required = false) Integer from,
                                                                     @RequestParam(defaultValue = "10", required = false) Integer size) {
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
