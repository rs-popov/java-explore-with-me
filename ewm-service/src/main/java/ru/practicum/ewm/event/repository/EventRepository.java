package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(" select e from Event e " +
            "where e.initiator.id = ?1 ")
    Page<Event> getEventsByInitiator(Long userId, Pageable page);


    @Query("select e from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and ((:text is null or upper(e.annotation) like upper(concat('%', :text, '%'))) " +
            "or (:text is null or upper(e.description) like upper(concat('%', :text, '%')))) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (:rangeStart is null or e.eventDate > :rangeStart)" +
            "and (:rangeEnd is null or e.eventDate < :rangeEnd)" +
            "and (:categories is null or e.category.id in :categories)")
    Page<Event> getEvents(@Param("text") String text,
                          @Param("categories") List<Long> categories,
                          @Param("paid") Boolean paid,
                          @Param("rangeStart") LocalDateTime rangeStart,
                          @Param("rangeEnd") LocalDateTime rangeEnd,
                          Pageable page);

    @Query("select e from Event e " +
            "where (:users is null or e.initiator.id in :users)" +
            "and (:categories is null or e.category.id in :categories)" +
            "and (:states is null or e.state in :states)" +
            "and (:rangeStart is null or e.eventDate > :rangeStart)" +
            "and (:rangeEnd is null or e.eventDate < :rangeEnd)")
    Page<Event> searchEvents(@Param("users") List<Long> users,
                             @Param("states") List<EventState> states,
                             @Param("categories") List<Long> categories,
                             @Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd,
                             Pageable page);


    @Query(" select e from Event e " +
            "where e.initiator.id = ?1 and e.id = ?2 ")
    Optional<Event> findEventByInitiator(Long userId, Long eventId);

    @Query(" select e from Event e " +
            "where e.initiator.id = ?1 ")
        //TODO
    Page<Event> getEventsByAdmin(Pageable page);

    @Query("select count(e) from Event e " +
            "where e.category.id = ?1 ")
    int getCountEventByCategoryId(Long catId);
}
