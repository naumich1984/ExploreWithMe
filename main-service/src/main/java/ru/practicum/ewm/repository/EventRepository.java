package ru.practicum.ewm.repository;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model.dto.EventFullFlatDto;
import ru.practicum.ewm.model.dto.EventRequestsConfirmDto;
import ru.practicum.ewm.model.dto.EventShortFlatDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(" select new ru.practicum.ewm.model.dto.EventShortFlatDto(e.annotation, " +
            " e.category.id, e.category.name, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.paid, e.title, e.participantLimit, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where e.initiator.id = :userId " +
            " group by e.annotation, e.category, e.eventDate, e.id, e.initiator, e.paid, e.title, e.participantLimit " +
            " order by e.eventDate desc " )
    List<EventShortFlatDto> findAllEventsWithRequestsCountByInitiatorId(@Param("userId") long userId, Pageable pageable);

    @Query(" select new ru.practicum.ewm.model.dto.EventFullFlatDto(e.annotation, " +
            " e.category.id, e.category.name," +
            " e.createdOn, e.description, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.lat, e.lon, e.paid, " +
            " e.participantLimit, e.publishedOn, e.requestModeration, " +
            " e.state , e.title, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where  e.initiator.id = :userId and e.id = :eventId " +
            " group by e.annotation, e.category, e.createdOn, e.description, e.eventDate, e.id, e.initiator," +
            " e.lat, e.lon, e.paid, e.participantLimit, e.publishedOn, e.requestModeration, e.state, e.title" +
            " order by e.eventDate desc " )
    Optional<EventFullFlatDto> findEventByIdAndUserIdWithRequestCount(@Param("userId") Long userId,
                                                                      @Param("eventId") Long eventId);


    @Query(" select r.id, r.status, r.created,  " +
            " r.requester.id, " +
            " r.event.id, r.event.state, r.event.participantLimit, r.event.requestModeration " +
            " from Request r " +
            " where r.id in :requestIds and r.event.id = :eventId " +
            " and r.requester.id = :userId and r.status = 'PENDING' " )
    Optional<List<EventRequestsConfirmDto>> findEventRequestsInfoForConfirmation(@Param("requestIds") List<Long> requestIds,
                                                                                 @Param("userId") Long userId,
                                                                                 @Param("eventId") Long eventId);


    @Query(" select new ru.practicum.ewm.model.dto.EventFullFlatDto(e.annotation, " +
            " e.category.id, e.category.name," +
            " e.createdOn, e.description, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.lat, e.lon, e.paid, " +
            " e.participantLimit, e.publishedOn, e.requestModeration, " +
            " e.state , e.title, count(r.id)) " +
            " from Request as r right join r.event as e where 1 = 1 " +
            " and (:users is null or e.initiator.id in :users ) " +
            " and (:states is null or e.state in :states) " +
            " and (:categories is null or e.category.id in :categories) " +
            " and (:rangeStart is null or e.eventDate >= :rangeStart) " +
            " and (:rangeEnd is null or e.eventDate <= :rangeEnd) " +
            " group by e.annotation, e.category.id, e.category.name, e.createdOn, e.description, e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.lat, e.lon, e.paid, e.participantLimit, e.publishedOn, e.requestModeration, e.state, e.title" +
            " order by e.eventDate desc " )
    List<EventFullFlatDto> findEventsByFilter(@Param("users") @Nullable List<Long> users,
                                              @Param("states") @Nullable List<EventState> states,
                                              @Param("categories") @Nullable List<Long> categories,
                                              @Param("rangeStart") @Nullable LocalDateTime rangeStart,
                                              @Param("rangeEnd") @Nullable LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select new ru.practicum.ewm.model.dto.EventShortFlatDto(e.annotation, " +
            " e.category.id, e.category.name, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.paid, e.title, e.participantLimit, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where 1 = 1 " +
            " and (:text is null or (lower(e.annotation) like lower(concat('%',:text,'%')) or lower(e.description) like lower(concat('%',:text,'%')))) " +
            " and (:categories is null or e.category.id in :categories) " +
            " and (:paid is null or e.paid in :paid ) " +
            " and (:rangeStart is null or e.eventDate > :rangeStart) " +
            " and (:rangeEnd is null or e.eventDate < :rangeEnd) " +
            " and ((:rangeEnd is null and :rangeStart is null) or e.eventDate > now()) " +
            " and e.state = 'PUBLISHED' " +
            " group by e.annotation, e.category.id, e.category.name, e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, e.paid, e.title, e.participantLimit " +
            " order by e.eventDate desc " )
    List<EventShortFlatDto> findEventsByFilterPublic(@Param("text") @Nullable String text,
                             @Param("categories") @Nullable List<Long> categories,
                             @Param("paid") @Nullable Boolean paid,
                             @Param("rangeStart") @Nullable LocalDateTime rangeStart,
                             @Param("rangeEnd") @Nullable LocalDateTime rangeEnd, Pageable pageable);

    @Query(" select new ru.practicum.ewm.model.dto.EventFullFlatDto(e.annotation, " +
            " e.category.id, e.category.name," +
            " e.createdOn, e.description, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.lat, e.lon, e.paid, " +
            " e.participantLimit, e.publishedOn, e.requestModeration, " +
            " e.state , e.title, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where e.id = :eventId and e.state = 'PUBLISHED' " +
            " group by e.annotation, e.category, e.createdOn, e.description, e.eventDate, e.id, e.initiator," +
            " e.lat, e.lon, e.paid, e.participantLimit, e.publishedOn, e.requestModeration, e.state, e.title" +
            " order by e.eventDate desc " )
    Optional<EventFullFlatDto> findEventByIdWithRequestCount(@Param("eventId") Long eventId);


    @Query(" select new ru.practicum.ewm.model.dto.EventShortFlatDto(e.annotation, " +
            " e.category.id, e.category.name, " +
            " e.eventDate, e.id, " +
            " e.initiator.id, e.initiator.name, " +
            " e.paid, e.title, e.participantLimit, count(r.id)) " +
            " from Request as r right join r.event as e " +
            " where e.id in :ids " +
            " group by e.annotation, e.category, e.eventDate, e.id, e.initiator, e.paid, e.title, e.participantLimit " +
            " order by e.eventDate desc " )
    Optional<List<EventShortFlatDto>> findAllEventsByIds(@Param("ids") List<Long> ids);

}
