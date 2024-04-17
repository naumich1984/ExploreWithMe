package ru.practicum.ewm.server.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.dto.stats.StatsDtoOut;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    //Не уникальные посещения для всех uri
    @Query("select new ru.practicum.ewm.dto.stats.StatsDtoOut(s.app, s.uri, count(s)) from Stats s " +
            " where s.timestamp >= :start and s.timestamp <= :end " +
            " group by s.app, s.uri order by 3 desc ")
    List<StatsDtoOut> findAllHitsByDatesNotUniqueForAllUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Уникальные посещения для всех uri
    @Query(value = "select ss.app, ss.uri, count(1) as hits from (select s.app, s.uri, count(1) as hits from stats as s " +
            " where s.time_event >= :start and s.time_event <= :end " +
            " group by s.app, s.uri, s.ip) as ss group by ss.app, ss.uri  order by 3 desc ", nativeQuery = true)
    List<StatsDtoOut> findAllHitsByDatesUniqueForAllUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    //Не уникальные посещения для списка uri
    @Query("select new ru.practicum.ewm.dto.stats.StatsDtoOut(s.app, s.uri, count(s)) from Stats s " +
            " where s.timestamp >= :start and s.timestamp <= :end " +
            " and s.uri in :uris group by s.app, s.uri  order by 3 desc ")
    List<StatsDtoOut> findAllHitsByDatesNotUniqueForListOfUris(@Param("start") LocalDateTime start,
                                                           @Param("end") LocalDateTime end, @Param("uris") List<String> uris);

    //Уникальные посещения для списка uri
    @Query(value = "select ss.app, ss.uri, count(1) as hits from (select s.app, s.uri, count(1) as hits from stats as s " +
            " where s.time_event >= :start and s.time_event <= :end and s.uri in (:uris)" +
            " group by s.app, s.uri, s.ip) as ss group by ss.app, ss.uri  order by 3 desc ", nativeQuery = true)
    List<StatsDtoOut> findAllHitsByDatesUniqueForListOfUris(@Param("start") LocalDateTime start,
                                                        @Param("end") LocalDateTime end, @Param("uris") List<String> uris);
}
