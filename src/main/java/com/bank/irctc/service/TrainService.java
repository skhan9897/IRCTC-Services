package com.bank.irctc.service;

import com.bank.irctc.entity.Train;
import com.bank.irctc.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    // =========================
    // GET ALL TRAINS
    // =========================
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    // =========================
    // GET TRAIN BY ID
    // =========================
    public Train getTrainById(Long id) {

        return trainRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Train not found with ID: " + id
                        )
                );
    }

    // =========================
    // SEARCH TRAIN
    // =========================
    public List<Train> searchTrain(
            String from,
            String to) {

        return trainRepository
                .findBySourceIgnoreCaseAndDestinationIgnoreCase(
                        from,
                        to
                );
    }

    // =========================
    // ADD TRAIN
    // =========================
    public Train addTrain(Train train) {

        if (train.getTrainNumber() == null ||
                train.getTrainNumber().isBlank()) {

            throw new RuntimeException(
                    "Train number is required"
            );
        }

        if (trainRepository.existsByTrainNumber(
                train.getTrainNumber())) {

            throw new RuntimeException(
                    "Train number already exists"
            );
        }

        if (train.getAvailableSeats() == null &&
                train.getTotalSeats() != null) {

            train.setAvailableSeats(
                    train.getTotalSeats()
            );
        }

        return trainRepository.save(train);
    }

    // =========================
    // UPDATE TRAIN
    // =========================
    public Train updateTrain(
            Long id,
            Train train) {

        Train existingTrain = getTrainById(id);

        existingTrain.setTrainNumber(
                train.getTrainNumber()
        );

        existingTrain.setTrainName(
                train.getTrainName()
        );

        existingTrain.setSource(
                train.getSource()
        );

        existingTrain.setDestination(
                train.getDestination()
        );

        existingTrain.setDepartureTime(
                train.getDepartureTime()
        );

        existingTrain.setArrivalTime(
                train.getArrivalTime()
        );

        existingTrain.setTotalSeats(
                train.getTotalSeats()
        );

        existingTrain.setAvailableSeats(
                train.getAvailableSeats()
        );

        existingTrain.setSleeperFare(
                train.getSleeperFare()
        );

        existingTrain.setAc3Fare(
                train.getAc3Fare()
        );

        existingTrain.setAc2Fare(
                train.getAc2Fare()
        );

        existingTrain.setAc1Fare(
                train.getAc1Fare()
        );

        return trainRepository.save(existingTrain);
    }

    // =========================
    // DELETE TRAIN
    // =========================
    public void deleteTrain(Long id) {

        Train train = getTrainById(id);

        trainRepository.delete(train);
    }
}