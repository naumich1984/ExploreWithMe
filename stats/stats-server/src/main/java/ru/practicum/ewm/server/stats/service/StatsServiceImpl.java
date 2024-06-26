package ru.practicum.ewm.server.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;
import ru.practicum.ewm.server.stats.model.StatsMapper;
import ru.practicum.ewm.server.stats.repository.StatsRepository;
import ru.practicum.ewm.server.stats.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public void saveHits(StatsDtoIn statsDtoIn) {
        log.debug("RUN saveHits");
        statsRepository.save(StatsMapper.toStatsFromInDto(statsDtoIn));
    }

    @Override
    public List<StatsDtoOut> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.debug("RUN getHits");
        if (start.isAfter(end)) {
            throw new BadRequestException("Start date must be early than end date!");
        }
        List<StatsDtoOut> hits;
        if (Optional.ofNullable(unique).orElse(false)) {
            if (uris == null) {
                log.debug(" | findAllHitsByDatesUniqueForAllUris");
                hits = statsRepository.findAllHitsByDatesUniqueForAllUris(start, end);
            } else {
                log.debug(" | findAllHitsByDatesUniqueForListOfUris");
                hits = statsRepository.findAllHitsByDatesUniqueForListOfUris(start, end, uris);
            }
        } else {
            if (uris == null) {
                log.debug(" | findAllHitsByDatesNotUniqueForAllUris");
                hits = statsRepository.findAllHitsByDatesNotUniqueForAllUris(start, end);
            } else {
                log.debug(" | findAllHitsByDatesNotUniqueForListOfUris");
                hits = statsRepository.findAllHitsByDatesNotUniqueForListOfUris(start, end, uris);
            }
        }

        return hits;
    }
}
