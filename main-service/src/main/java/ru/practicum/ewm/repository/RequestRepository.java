package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model._enum.RequestStatus;
import ru.practicum.ewm.model.dto.EventRequestsStatDto;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(" select r.id as cnt from Request r where r.id in :ids and r.status != 'PENDING' ")
    List<Long> findCountRequestsWithWrongState(@Param("ids") List<Long> ids, Pageable pageable);

    @Modifying
    @Query(" update Request r set r.status = :status where r.id in :ids ")
    void updateRequestStatusByIds(@Param("ids") List<Long> ids, RequestStatus status);

    @Query(" select new ru.practicum.ewm.model.dto.ParticipationRequestDto(r.created, " +
            " r.event.id, r.id, r.requester.id, r.status) " +
            " from Request r where r.id in :ids order by r.created ")
    List<ParticipationRequestDto> findAllRequestByIds(@Param("ids") List<Long> ids);

    @Query(" select new ru.practicum.ewm.model.dto.ParticipationRequestDto(r.created, " +
            " r.event.id, r.id, r.requester.id, r.status) " +
            " from Request r where r.requester.id in :userId order by r.created ")
    List<ParticipationRequestDto> findAllRequestByUserId(@Param("userId") Long userId);

    @Query(" select count(r.id) from Request r where r.event.id = :eventId and r.status = 'CONFIRMED' ")
    Long findCountConfirmedRequestByEventId(@Param("eventId") Long eventId);

    @Query(" select r from Request r where r.event.id = :eventId and r.requester.id = :userId ")
    Optional<Request> findByEventIdAndUserId(@Param("eventId") Long eventId, @Param("userId") Long userId);

    @Query(" select r from Request r where r.id = :requestId and r.requester.id = :userId ")
    Optional<Request> findByRequestIdAndUserId(@Param("requestId") Long requestId, @Param("userId") Long userId);

    @Query(" select new ru.practicum.ewm.model.dto.ParticipationRequestDto(r.created, " +
            " r.event.id, r.id, r.requester.id, r.status) " +
            " from Request r " +
            " where r.event.id = :eventId and r.event.initiator.id = :userId order by r.created desc ")
    Optional<List<ParticipationRequestDto>> findEventRequestsByUserIdAndEventId(@Param("userId") Long userId,
                                                      @Param("eventId") Long eventId);

    @Query(" select new ru.practicum.ewm.model.dto.EventRequestsStatDto(r.event.participantLimit, " +
            " r.event.requestModeration, r.status, count(r.id)) " +
            " from Request r " +
            " where r.id in :requestIds and r.event.id = :eventId " +
            " and r.event.initiator.id = :userId " +
            " group by r.event.participantLimit, r.event.requestModeration, r.status ")
    Optional<List<EventRequestsStatDto>> findEventRequestsInfo(@Param("requestIds") List<Long> requestIds,
                                                         @Param("userId") Long userId,
                                                         @Param("eventId") Long eventId);

}
