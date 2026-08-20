package com.bank.irctc.repository;

import com.bank.irctc.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    // Find station by station code
    Optional<Station> findByStationCode(String stationCode);

    // Find stations by city
    List<Station> findByCity(String city);

    // Find stations by state
    List<Station> findByState(String state);

    // Search station by name
    List<Station> findByStationNameContainingIgnoreCase(String stationName);

    // Check station code
    boolean existsByStationCode(String stationCode);
}