package com.bank.irctc.service;

import com.bank.irctc.entity.Station;
import com.bank.irctc.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    // =========================
    // GET ALL STATIONS
    // =========================
    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    // =========================
    // GET STATION BY ID
    // =========================
    public Station getStationById(Long id) {

        return stationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Station not found with ID: " + id
                        )
                );
    }

    // =========================
    // ADD STATION
    // =========================
    public Station addStation(Station station) {

        if (station.getStationCode() == null ||
                station.getStationCode().isBlank()) {

            throw new RuntimeException(
                    "Station code is required"
            );
        }

        if (stationRepository.existsByStationCode(
                station.getStationCode())) {

            throw new RuntimeException(
                    "Station code already exists"
            );
        }

        return stationRepository.save(station);
    }

    // =========================
    // UPDATE STATION
    // =========================
    public Station updateStation(
            Long id,
            Station station) {

        Station existingStation =
                getStationById(id);

        existingStation.setStationName(
                station.getStationName()
        );

        existingStation.setCity(
                station.getCity()
        );

        existingStation.setState(
                station.getState()
        );

        return stationRepository.save(
                existingStation
        );
    }

    // =========================
    // DELETE STATION
    // =========================
    public void deleteStation(Long id) {

        Station station =
                getStationById(id);

        stationRepository.delete(station);
    }
}