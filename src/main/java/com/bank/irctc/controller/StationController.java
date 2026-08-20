package com.bank.irctc.controller;

import com.bank.irctc.entity.Station;
import com.bank.irctc.service.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@CrossOrigin(origins = "*")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    // =========================
    // GET ALL STATIONS
    // =========================
    @GetMapping
    public ResponseEntity<List<Station>> getAllStations() {

        List<Station> stations = stationService.getAllStations();

        return ResponseEntity.ok(stations);
    }

    // =========================
    // GET STATION BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Station> getStationById(
            @PathVariable Long id) {

        Station station = stationService.getStationById(id);

        return ResponseEntity.ok(station);
    }

    // =========================
    // ADD STATION
    // =========================
    @PostMapping
    public ResponseEntity<Station> addStation(
            @RequestBody Station station) {

        Station savedStation = stationService.addStation(station);

        return ResponseEntity.ok(savedStation);
    }

    // =========================
    // UPDATE STATION
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Station> updateStation(
            @PathVariable Long id,
            @RequestBody Station station) {

        Station updatedStation =
                stationService.updateStation(id, station);

        return ResponseEntity.ok(updatedStation);
    }

    // =========================
    // DELETE STATION
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStation(
            @PathVariable Long id) {

        stationService.deleteStation(id);

        return ResponseEntity.ok(
                "Station deleted successfully"
        );
    }
}