package ru.practicum.ewm.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.location.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.lat = ?1 and l.lon = ?2 ")
    Optional<Location> findLocation(Double lat, Double lon);

    @Query("select l from Location l " +
            "where :description is null or upper(l.description) like upper(concat('%', :description, '%'))")
    Page<Location> findLocations(@Param("description") String description, @Param("page") Pageable page);

    @Query(value = "select * from locations " +
            "where distance(:lat, :lon, lat, lon) < (:distance+radius)", nativeQuery = true)
    List<Location> searchLocations(@Param("lat") Double lat,
                                   @Param("lon") Double lon,
                                   @Param("distance") Double distance);
}
