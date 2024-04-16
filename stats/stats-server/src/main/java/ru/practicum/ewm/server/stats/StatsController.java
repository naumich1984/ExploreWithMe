package ru.practicum.ewm.server.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Integer> addHit(@RequestBody StatsDtoIn statsDtoIn) {

        return new ResponseEntity<Integer>(statsService.saveHits(statsDtoIn), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDtoOut>> getStats(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                      @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                      @RequestParam(value = "uris", required = false) List<String> uris,
                                                      @RequestParam(value = "unique", required = false) Boolean unique) {

        return ResponseEntity.ok(statsService.getHits(start, end, uris, unique));
    }

}
