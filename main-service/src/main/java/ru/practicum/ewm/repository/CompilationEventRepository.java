package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.CompilationEvent;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {

    void deleteAllByCompilationIdAndEventIdIn(Long compId, List<Long> eventIdsToDel);

    void deleteAllByCompilationId(Long compId);
}
