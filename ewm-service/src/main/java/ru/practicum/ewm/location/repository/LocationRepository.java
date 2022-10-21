package ru.practicum.ewm.location.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.model.dto.LocationOutputDtoWithDistance;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.lat = ?1 and l.lon = ?2 ")
    Optional<Location> findLocationByPoint(Double lat, Double lon);

    @Query(nativeQuery = true,
            value = "select * from locations " +
                    "where (distance(:lat, :lon, lat, lon) < (:distance))" +
                    "and (:name is null or upper(name) like upper(concat('%', :name, '%')))")
    Optional<Location> findLocationByPointWithDistAndName(@Param("lat") Double lat,
                                                          @Param("lon") Double lon,
                                                          @Param("distance") Double distance,
                                                          @Param("name") String name);

    @Query(nativeQuery = true,
            value = "select *,distance(:lat, :lon, lat, lon) as distance from locations " +
                    "where (distance(:lat, :lon, lat, lon) < (:distance+radius))" +
                    "and (:name is null or upper(name) like upper(concat('%', :name, '%')))" +
                    "and (:description is null or upper(description) like upper(concat('%', :description, '%')))" +
                    "order by distance asc ")
    Page<LocationOutputDtoWithDistance> searchLocations(@Param("lat") Double lat,
                                                        @Param("lon") Double lon,
                                                        @Param("distance") Double distance,
                                                        @Param("name") String name,
                                                        @Param("description") String description,
                                                        @Param("page") Pageable page);

    @Query(nativeQuery = true,
            value = "select *,distance(:lat, :lon, lat, lon) as distance from locations " +
                    "where (distance(:lat, :lon, lat, lon) < (:distance+radius))")
    List<Location> getLocations(@Param("lat") Double lat,
                                @Param("lon") Double lon,
                                @Param("distance") Double distance);
}