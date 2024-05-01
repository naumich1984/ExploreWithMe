package ru.practicum.ewm.server.stats.service;

import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveHits(StatsDtoIn statsDtoIn);

    List<StatsDtoOut> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
