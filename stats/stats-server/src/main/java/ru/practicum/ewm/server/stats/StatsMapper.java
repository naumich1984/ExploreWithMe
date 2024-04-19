package ru.practicum.ewm.server.stats;

import ru.practicum.ewm.dto.stats.StatsDtoIn;

public class StatsMapper {

    public static Stats toStatsFromInDto(StatsDtoIn stats) {

        return Stats.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }
}
