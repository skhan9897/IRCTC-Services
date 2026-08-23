package com.bank.irctc.repository;

import com.bank.irctc.entity.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainScheduleRepository
        extends JpaRepository<TrainSchedule, Long> {

    // Train ke saare schedules
    List<TrainSchedule> findByTrainId(Long trainId);

    // Particular date ke schedules
    List<TrainSchedule> findByJourneyDate(
            LocalDate journeyDate
    );

    // Train + Journey Date
    List<TrainSchedule> findByTrainIdAndJourneyDate(
            Long trainId,
            LocalDate journeyDate
    );

    // Status ke according schedules
    List<TrainSchedule> findByStatus(String status);

    // Search by Source, Destination and Date
    List<TrainSchedule> findByTrainSourceIgnoreCaseAndTrainDestinationIgnoreCaseAndJourneyDate(
            String source,
            String destination,
            LocalDate journeyDate
    );
}