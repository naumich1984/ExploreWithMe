package ru.practicum.ewm.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Component
public class StatsInfo {

    private final ObjectMapper objectMapper;
    private final StatsClient statsClient;

    public Long getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<Object> response = statsClient.getHits(start, end, uris, unique);
        List<StatsDtoOut> stats = objectMapper.convertValue(response.getBody(), new TypeReference<List<StatsDtoOut>>() {});
        if (!stats.isEmpty()) {
            return stats.get(0).getHits();
        }

        return 0L;
    }

    public void addHits(StatsDtoIn statsDtoIn) {
        statsClient.addHits(statsDtoIn);
    }
}
