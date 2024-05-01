package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(" select c from Compilation c where c.pinned = :pinned ")
    List<Compilation> findAllByPinned(@Param("pinned") Boolean pinned, Pageable pageable);

    @Query(" select c.event.id from CompilationEvent c where c.compilation.id = :compId ")
    Optional<List<Long>> findAllEventIdsByCompilationId(@Param("compId") Long compId);
}
