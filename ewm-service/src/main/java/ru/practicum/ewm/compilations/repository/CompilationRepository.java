package ru.practicum.ewm.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Modifying
    @Query(value = "insert into events_compilations (event_id, compilation_id) values (:eventId, :compId)", nativeQuery = true)
    @Transactional
    int addEventToCompilation(@Param("compId") Long compId, @Param("eventId") Long eventId);

    @Modifying
    @Query(value = "delete from events_compilations " +
            "where event_id = :eventId and compilation_id = :compId", nativeQuery = true)
    @Transactional
    int deleteEventToCompilation(@Param("compId") Long compId, @Param("eventId") Long eventId);

    @Query("select c from Compilation c where (:pinned is null or c.pinned = :pinned) ")
    Page<Compilation> getCompilations(@Param("pinned") Boolean pinned, Pageable page);
}