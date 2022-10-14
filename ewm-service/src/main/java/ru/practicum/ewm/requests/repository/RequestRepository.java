package ru.practicum.ewm.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.requests.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(" select r from Request r " +
            "where r.user.id = ?1 " +
            "order by r.created desc")
    List<Request> findRequestsByUserId(Long userId);

    @Query("select count(r) from Request r " +
            "where r.event.id = ?1  and  r.status = 'CONFIRMED' ")
    Long getCountConfirmedRequestByEventId(Long eventId);

    @Query(" select r from Request r " +
            "where r.user.id = ?1 and r.event.id = ?2 ")
    Optional<Request> findRequestByUserIdAndEventId(Long userId, Long eventId);

    @Query(" select r from Request r " +
            "where r.event.id = ?1 ")
    List<Request> findRequestByEventId(Long eventId);
}