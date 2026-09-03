package com.bank.irctc.service;

import com.bank.irctc.entity.Train;
import com.bank.irctc.entity.TrainSchedule;
import com.bank.irctc.repository.TrainRepository;
import com.bank.irctc.repository.TrainScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainScheduleService {

    private final TrainScheduleRepository scheduleRepository;
    private final TrainRepository trainRepository;

    public TrainScheduleService(
            TrainScheduleRepository scheduleRepository,
            TrainRepository trainRepository) {

        this.scheduleRepository = scheduleRepository;
        this.trainRepository = trainRepository;
    }

    // =========================
    // GET ALL SCHEDULES
    // =========================
    public List<TrainSchedule> getAllSchedules() {

        return scheduleRepository.findAll();
    }

    // =========================
    // GET SCHEDULE BY ID
    // =========================
    public TrainSchedule getScheduleById(Long id) {

        return scheduleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Train schedule not found with ID: " + id
                        )
                );
    }

    // =========================
    // GET SCHEDULES BY TRAIN
    // =========================
    public List<TrainSchedule> getSchedulesByTrain(Long trainId) {

        if (!trainRepository.existsById(trainId)) {
            throw new RuntimeException(
                    "Train not found with ID: " + trainId
            );
        }

        return scheduleRepository.findByTrainId(trainId);
    }

    // =========================
    // GET SCHEDULES BY DATE
    // =========================
    public List<TrainSchedule> getSchedulesByDate(
            LocalDate journeyDate) {

        return scheduleRepository.findByJourneyDate(
                journeyDate
        );
    }

    // =========================
    // SEARCH SCHEDULES
    // =========================
    public List<TrainSchedule> searchSchedules(
            String from,
            String to,
            LocalDate date) {

        String fromNorm = from.trim();
        String toNorm = to.trim();
        
        System.out.println("Searching Schedules: From=" + fromNorm + ", To=" + toNorm + ", Date=" + date);
        
        // 1. Try Exact Match in Schedules
        List<TrainSchedule> results = scheduleRepository
                .findByTrainSourceIgnoreCaseAndTrainDestinationIgnoreCaseAndJourneyDate(
                        fromNorm,
                        toNorm,
                        date
                );
        
        // 2. Fallback: Try Exact Match in Trains and wrap in Mock Schedules
        if (results.isEmpty()) {
            List<Train> trains = trainRepository.findBySourceIgnoreCaseAndDestinationIgnoreCase(fromNorm, toNorm);
            for (Train t : trains) {
                results.add(createMockSchedule(t, date));
            }
        }
        
        // 3. Last Resort: Try Partial Match (Containing) in Trains
        if (results.isEmpty()) {
            List<Train> allTrains = trainRepository.findAll();
            for (Train t : allTrains) {
                boolean sourceMatch = t.getSource().toLowerCase().contains(fromNorm.toLowerCase()) || 
                                    fromNorm.toLowerCase().contains(t.getSource().toLowerCase());
                boolean destMatch = t.getDestination().toLowerCase().contains(toNorm.toLowerCase()) || 
                                  toNorm.toLowerCase().contains(t.getDestination().toLowerCase());
                                  
                if (sourceMatch && destMatch) {
                    results.add(createMockSchedule(t, date));
                }
            }
        }
        
        return results;
    }

    private TrainSchedule createMockSchedule(Train t, LocalDate date) {
        TrainSchedule mock = new TrainSchedule();
        mock.setTrain(t);
        mock.setJourneyDate(date);
        mock.setAvailableSeats(t.getAvailableSeats() != null ? t.getAvailableSeats() : 100);
        mock.setTotalSeats(t.getTotalSeats() != null ? t.getTotalSeats() : 100);
        mock.setStatus("ACTIVE");
        return mock;
    }

    // =========================
    // ADD SCHEDULE
    // =========================
    public TrainSchedule addSchedule(
            Long trainId,
            TrainSchedule schedule) {

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Train not found with ID: " + trainId
                        )
                );

        schedule.setTrain(train);

        if (schedule.getStatus() == null ||
                schedule.getStatus().isBlank()) {

            schedule.setStatus("ACTIVE");
        }

        if (schedule.getTotalSeats() == null) {
            schedule.setTotalSeats(
                    train.getTotalSeats()
            );
        }

        if (schedule.getAvailableSeats() == null) {
            schedule.setAvailableSeats(
                    schedule.getTotalSeats()
            );
        }

        return scheduleRepository.save(schedule);
    }

    // =========================
    // UPDATE SCHEDULE
    // =========================
    public TrainSchedule updateSchedule(
            Long id,
            TrainSchedule schedule) {

        TrainSchedule existing =
                getScheduleById(id);

        existing.setJourneyDate(
                schedule.getJourneyDate()
        );

        existing.setDepartureTime(
                schedule.getDepartureTime()
        );

        existing.setArrivalTime(
                schedule.getArrivalTime()
        );

        existing.setTotalSeats(
                schedule.getTotalSeats()
        );

        existing.setAvailableSeats(
                schedule.getAvailableSeats()
        );

        existing.setStatus(
                schedule.getStatus()
        );

        return scheduleRepository.save(existing);
    }

    // =========================
    // DELETE SCHEDULE
    // =========================
    public void deleteSchedule(Long id) {

        TrainSchedule schedule =
                getScheduleById(id);

        scheduleRepository.delete(schedule);
    }
}