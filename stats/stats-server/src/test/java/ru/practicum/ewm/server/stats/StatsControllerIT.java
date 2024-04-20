package ru.practicum.ewm.server.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;
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

    @SneakyThrows
    @Test
    void addHit_whenHitValid_thenReturnedCreated() {
        when(statsService.saveHits(statsDtoIn)).thenReturn(stats);

        int result = mockMvc.perform(post("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stats)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getStatus();

        assertEquals(objectMapper.writeValueAsString(stats), result);
    }

    @Test
    void getStats() {
    }
}