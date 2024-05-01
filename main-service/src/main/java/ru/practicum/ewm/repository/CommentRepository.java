package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.dto.CommentFlatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(" select new ru.practicum.ewm.model.dto.CommentFlatDto(" +
            " c.id, c.event.id, c.created, c.changed, c.author.id, c.author.name, c.text) " +
            " from Comment c " +
            " where c.event.id = :eventId " +
            " and (cast(:rangeStart as timestamp) is null or c.created >= :rangeStart) " +
            " and (cast(:rangeEnd as timestamp) is null or c.created <= :rangeEnd) " +
            " and (:text is null or (lower(c.text) like lower(concat('%',:text,'%')))) " +
            " order by c.created desc ")
    List<CommentFlatDto> findAllCommentsByFilter(@Param("eventId") Long eventId,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                 @Param("text") String text,
                                                 Pageable pageable);
}
