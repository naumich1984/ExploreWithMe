package ru.practicum.ewm.server.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

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
    public Integer saveHits(StatsDtoIn statsDtoIn) {
        Stats hit = StatsMapper.toStatsFromInDto(statsDtoIn);
        statsRepository.save(hit);

        return 1;
    }

    @Override
    public List<StatsDtoOut> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsDtoOut> hits = List.of();
        if (Optional.ofNullable(unique).orElse(false)) {
            if(uris == null) {
                //return hits;
                hits = statsRepository.findAllHitsByDatesUniqueForAllUris(start, end);
            } else {
                hits = statsRepository.findAllHitsByDatesUniqueForListOfUris(start, end, uris);
            }
        } else {
            if(uris == null) {
                //return hits;
                hits = statsRepository.findAllHitsByDatesNotUniqueForAllUris(start, end);
            } else {
                hits = statsRepository.findAllHitsByDatesNotUniqueForListOfUris(start, end, uris);
            }
        }

        return hits;
    }
}
