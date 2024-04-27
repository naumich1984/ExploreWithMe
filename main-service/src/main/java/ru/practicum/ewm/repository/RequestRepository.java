package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model._enum.RequestStatus;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(" update Request r set r.status = :status where r.id in :ids ")
    void updateRequestStatusByIds(@Param("ids") List<Long> ids, RequestStatus status);

    @Query(" select r.created, r.event.id, r.id, r.status from Request r where r.id in :ids order by r.created ")
    List<ParticipationRequestDto> findAllRequestByIds(@Param("ids") List<Long> ids);
}
