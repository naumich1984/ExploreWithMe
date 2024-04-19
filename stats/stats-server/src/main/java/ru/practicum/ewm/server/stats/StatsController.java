package ru.practicum.ewm.server.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Integer> addHit(@RequestBody @Valid StatsDtoIn statsDtoIn) {
        log.debug("POST /hit");
        log.debug(" | app:{}", statsDtoIn.getApp());
        log.debug(" | ip:{}", statsDtoIn.getIp());
        log.debug(" | uri:{}", statsDtoIn.getUri());
        log.debug(" | timestamp:{}", statsDtoIn.getTimestamp());

        return new ResponseEntity<Integer>(statsService.saveHits(statsDtoIn), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoOut>> getStats(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                      @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                      @RequestParam(value = "uris", required = false) List<String> uris,
                                                      @RequestParam(value = "unique", required = false) Boolean unique) {
        log.debug("GET /stats");
        log.debug(" | start:{}", start);
        log.debug(" | end:{}", end);
        log.debug(" | uris:{}", uris == null ? "null" : uris.toString());
        log.debug(" | unique:{}", unique == null ? "null" : unique);

        return ResponseEntity.ok(statsService.getHits(start, end, uris, unique));
    }
}
