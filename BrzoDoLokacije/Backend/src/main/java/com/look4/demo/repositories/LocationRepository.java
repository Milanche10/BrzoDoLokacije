package com.look4.demo.repositories;

import com.look4.demo.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    /*@Query("SELECT l FROM location l WHERE l.lon = ?1  and l.lat = ?2")
    Optional<Location> FindLocationByCordinates(Double lon, Double lat);*/

    Location findLocationByLonAndLat(double lon, double lat);
}
