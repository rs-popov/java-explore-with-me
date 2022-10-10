package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(nativeQuery = true, value =
            "select jsonb_extract_path_text(h.attributes, 'app') as app, h.uri as uri, count(h.uri) as hits " +
                    "from statistics h " +
                    "where ((created between cast(:start as timestamp) and cast(:end as timestamp)) " +
                    "and (:uris is null or h.uri in :uris))" +
                    "group by app, uri")
    List<ViewStats> getViewWithDuplicateIP(@Param("start") String start,
                                           @Param("end") String end,
                                           @Param("uris") List<String> uris);

    @Query(nativeQuery = true, value =
            "select jsonb_extract_path_text(h.attributes, 'app') as app, h.uri as uri, count(h.uri) as hits " +
                    "from (select distinct(attributes) as attributes, uri, max(created) as created from statistics group by attributes, uri) h " +
                    "where ((created between cast(:start as timestamp) and cast(:end as timestamp)) " +
                    "and (:uris is null or h.uri in :uris))" +
                    "group by app, uri")
    List<ViewStats> getViewWithUniqueIP(@Param("start") String start,
                                        @Param("end") String end,
                                        @Param("uris") List<String> uris);
}