package ru.practicum.ewm.server.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsControllerTest {

    @Mock
    private StatsService statsService;

    @InjectMocks
    private StatsController statsController;
    private Stats stats;
    private StatsDtoIn statsDtoIn;
    private StatsDtoOut statsDtoOut;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<String> uris;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        start = now;
        end = now.plusDays(1);
        uris = List.of("uri");
        statsDtoOut = new StatsDtoOut("app", "uri", 13L);
        statsDtoIn = new StatsDtoIn("app", "uri", "127.0.0.1", now);
        stats = StatsMapper.toStatsFromInDto(statsDtoIn);
    }

    @Test
    void addHit() {
        doReturn(stats).when(statsService).saveHits(statsDtoIn);

        ResponseEntity<Stats> response = statsController.addHit(statsDtoIn);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getStats() {
        List<StatsDtoOut> expectedHits = List.of(statsDtoOut);
        when(statsService.getHits(start, end, uris, true)).thenReturn(expectedHits);

        ResponseEntity<List<StatsDtoOut>> response = statsController.getStats(start, end, uris, true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedHits.get(0).getHits(), response.getBody().get(0).getHits());
    }
}