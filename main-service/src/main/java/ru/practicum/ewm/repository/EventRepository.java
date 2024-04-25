package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.dto.EventShortFlatDto;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(" select new ru.practicum.ewm.model.dto.EventShortFlatDto(e.annotation, " +
            " e.category.id, e.category.name, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.paid, e.title, e.id, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where e.initiator.id = :userId " +
            " group by e.annotation, e.category, e.eventDate, e.id, e.initiator, e.paid, e.title, e.id " +
            " order by e.eventDate desc " )
    List<EventShortFlatDto> findAllEventsWithRequestsCountByInitiatorId(@Param("userId") long userId, Pageable pageable);
}
