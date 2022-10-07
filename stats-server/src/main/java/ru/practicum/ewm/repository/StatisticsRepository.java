package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.dto.ViewStatsDtoTest;
import ru.practicum.ewm.model.EndpointHit;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit,Long> {

    @Query(value = "select * from statistics " +
            "where created between cast(:start as timestamp) and cast(:end as timestamp) ", nativeQuery = true)
    List<EndpointHit> getStats(@Param("start") String start,
                               @Param("end") String end);

    @Query(nativeQuery = true, value = "select h.uri as uri, count(h.uri) as hits from statistics h group by h.uri")
    List<ViewStatsDtoTest> getView();

    @Query(nativeQuery = true, value =
            "select jsonb_extract_path_text(h.attributes, 'app') as app, h.uri as uri, count(h.uri) as hits " +
                    "from statistics h " +
                    "where ((created between cast(:start as timestamp) and cast(:end as timestamp)) " +
                    "and (:uris is null or h.uri in :uris))" +
                    "group by app, uri")
    List<ViewStatsDtoTest> getView2(@Param("start") String start,
                                    @Param("end") String end,
                                    @Param("uris") List<String> uris);

    @Query(nativeQuery = true, value =
            "select jsonb_extract_path_text(h.attributes, 'app') as app, h.uri as uri, count(h.uri) as hits " +
                    "from (select distinct(attributes) as attributes, uri, max(created) as created from statistics group by attributes, uri) h " +
                    "where ((created between cast(:start as timestamp) and cast(:end as timestamp)) " +
                    "and (:uris is null or h.uri in :uris))" +
                    "group by app, uri")
    List<ViewStatsDtoTest> getView3(@Param("start") String start,
                                    @Param("end") String end,
                                    @Param("uris") List<String> uris);


}

    //JSON_VALUE(BookCategory,'$.Name') AS Name
/**
 * public interface SurveyRepository extends CrudRepository<Survey, Long> {
 *     @Query(nativeQuery = true, value =
 *            "SELECT " +
 *            "    v.answer AS answer, COUNT(v) AS cnt " +
 *            "FROM " +
 *            "    Survey v " +
 *            "GROUP BY " +
 *            "    v.answer")
 *     List<SurveyAnswerStatistics> findSurveyCount();
 * }
 */