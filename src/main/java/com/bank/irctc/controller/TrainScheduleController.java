package com.bank.irctc.controller;

import com.bank.irctc.entity.TrainSchedule;
import com.bank.irctc.service.TrainScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = "*")
public class TrainScheduleController {

    private final TrainScheduleService scheduleService;

    public TrainScheduleController(
            TrainScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // =========================
    // GET ALL SCHEDULES
    // =========================
    @GetMapping
    public ResponseEntity<List<TrainSchedule>> getAllSchedules() {

        return ResponseEntity.ok(
                scheduleService.getAllSchedules()
        );
    }

    // =========================
    // GET SCHEDULE BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<TrainSchedule> getScheduleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                scheduleService.getScheduleById(id)
        );
    }

    // =========================
    // GET SCHEDULES BY TRAIN
    // =========================
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<TrainSchedule>>
    getSchedulesByTrain(
            @PathVariable Long trainId) {

        return ResponseEntity.ok(
                scheduleService.getSchedulesByTrain(trainId)
        );
    }

    // =========================
    // GET SCHEDULES BY DATE
    // =========================
    @GetMapping("/date/{date}")
    public ResponseEntity<List<TrainSchedule>>
    getSchedulesByDate(
            @PathVariable LocalDate date) {

        return ResponseEntity.ok(
                scheduleService.getSchedulesByDate(date)
        );
    }

    // =========================
    // ADD SCHEDULE
    // =========================
    @PostMapping("/train/{trainId}")
    public ResponseEntity<TrainSchedule> addSchedule(
            @PathVariable Long trainId,
            @RequestBody TrainSchedule schedule) {

        return ResponseEntity.ok(
                scheduleService.addSchedule(
                        trainId,
                        schedule
                )
        );
    }

    // =========================
    // UPDATE SCHEDULE
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<TrainSchedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody TrainSchedule schedule) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(
                        id,
                        schedule
                )
        );
    }

    // =========================
    // DELETE SCHEDULE
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchedule(
            @PathVariable Long id) {

        scheduleService.deleteSchedule(id);

        return ResponseEntity.ok(
                "Train schedule deleted successfully"
        );
    }
}