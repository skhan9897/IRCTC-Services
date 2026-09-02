package com.bank.irctc.controller;

import com.bank.irctc.entity.Train;
import com.bank.irctc.service.TrainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
public class TrainController {

    private final TrainService trainService;

    public TrainController(TrainService trainService) {
        this.trainService = trainService;
    }

    // =========================
    // GET ALL TRAINS
    // =========================
    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {

        List<Train> trains = trainService.getAllTrains();

        return ResponseEntity.ok(trains);
    }

    // =========================
    // GET TRAIN BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(
            @PathVariable Long id) {

        Train train = trainService.getTrainById(id);

        return ResponseEntity.ok(train);
    }

    // =========================
    // SEARCH TRAIN
    // =========================
    @GetMapping("/search")
    public ResponseEntity<List<Train>> searchTrain(
            @RequestParam String from,
            @RequestParam String to) {

        List<Train> trains =
                trainService.searchTrain(from, to);

        return ResponseEntity.ok(trains);
    }

    // =========================
    // SEARCH BY NAME OR NUMBER
    // =========================
    @GetMapping("/find")
    public ResponseEntity<List<Train>> findByNameOrNumber(
            @RequestParam String query) {

        List<Train> trains = trainService.searchByNameOrNumber(query);

        return ResponseEntity.ok(trains);
    }

    // =========================
    // ADD TRAIN
    // =========================
    @PostMapping
    public ResponseEntity<Train> addTrain(
            @RequestBody Train train) {

        Train savedTrain = trainService.addTrain(train);

        return ResponseEntity.ok(savedTrain);
    }

    // =========================
    // UPDATE TRAIN
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(
            @PathVariable Long id,
            @RequestBody Train train) {

        Train updatedTrain =
                trainService.updateTrain(id, train);

        return ResponseEntity.ok(updatedTrain);
    }

    // =========================
    // DELETE TRAIN
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTrain(
            @PathVariable Long id) {

        trainService.deleteTrain(id);

        return ResponseEntity.ok(
                "Train deleted successfully"
        );
    }
}