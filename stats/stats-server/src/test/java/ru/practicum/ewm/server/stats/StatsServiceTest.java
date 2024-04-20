package ru.practicum.ewm.server.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsServiceImpl statsService;
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
        stats.setId(1L);
    }

    @Test
    void saveHits() {
        doReturn(stats).when(statsRepository).save(StatsMapper.toStatsFromInDto(statsDtoIn));

        Stats actual = statsService.saveHits(statsDtoIn);

        assertEquals(stats, actual);
    }

    @Test
    void getHits_whenInvokedOnlyDates_thenReturnedHits() {
        List<StatsDtoOut> expectedHits = List.of(statsDtoOut);
        when(statsRepository.findAllHitsByDatesNotUniqueForAllUris(start, end)).thenReturn(expectedHits);

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, null, null);

        assertEquals(expectedHits.get(0).getHits(), actualHits.get(0).getHits());
    }

    @Test
    void getHits_whenInvokedOnlyDatesWithoutStats_thenReturnedEmptyList() {
        start = start.minusDays(3);
        end = end.minusDays(1);
        when(statsRepository.findAllHitsByDatesNotUniqueForAllUris(start, end)).thenReturn(List.of());

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, null, null);

        assertEquals(List.of(), actualHits);
    }

    @Test
    void getHits_whenInvokedWithDatesAndUris_thenReturnedHits() {
        List<StatsDtoOut> expectedHits = List.of(statsDtoOut);
        when(statsRepository.findAllHitsByDatesNotUniqueForListOfUris(start, end, uris)).thenReturn(expectedHits);

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, uris, null);

        assertEquals(expectedHits.get(0).getHits(), actualHits.get(0).getHits());
    }

    @Test
    void getHits_whenInvokedDatesWithoutStatsOrWrongUris_thenReturnedEmptyList() {
        start = start.minusDays(3);
        end = end.minusDays(1);
        when(statsRepository.findAllHitsByDatesNotUniqueForListOfUris(start, end, uris)).thenReturn(List.of());

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, uris, null);

        assertEquals(List.of(), actualHits);
    }

    @Test
    void getHits_whenInvokedWithDatesAndUnique_thenReturnedHits() {
        List<StatsDtoOut> expectedHits = List.of(statsDtoOut);
        when(statsRepository.findAllHitsByDatesUniqueForAllUris(start, end)).thenReturn(expectedHits);

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, null, true);

        assertEquals(expectedHits.get(0).getHits(), actualHits.get(0).getHits());
    }

    @Test
    void getHits_whenInvokedDatesWithoutStatsAndUnique_thenReturnedEmptyList() {
        start = start.minusDays(3);
        end = end.minusDays(1);
        when(statsRepository.findAllHitsByDatesUniqueForAllUris(start, end)).thenReturn(List.of());

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, null, true);

        assertEquals(List.of(), actualHits);
    }

    @Test
    void getHits_whenInvokedWithDatesAndUrisAndUnique_thenReturnedHits() {
        List<StatsDtoOut> expectedHits = List.of(statsDtoOut);
        when(statsRepository.findAllHitsByDatesUniqueForListOfUris(start, end, uris)).thenReturn(expectedHits);

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, uris, true);

        assertEquals(expectedHits.get(0).getHits(), actualHits.get(0).getHits());
    }

    @Test
    void getHits_whenInvokedDatesWithoutStatsOrWrongUrisAndUrisAndUnique_thenReturnedEmptyList() {
        start = start.minusDays(3);
        end = end.minusDays(1);
        when(statsRepository.findAllHitsByDatesUniqueForListOfUris(start, end, uris)).thenReturn(List.of());

        List<StatsDtoOut> actualHits = statsService.getHits(start, end, uris, true);

        assertEquals(List.of(), actualHits);
    }
}