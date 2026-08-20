package com.bank.irctc.repository;

import com.bank.irctc.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {

    // Find train by train number
    Optional<Train> findByTrainNumber(String trainNumber);

    // Search trains by source and destination
    List<Train> findBySourceIgnoreCaseAndDestinationIgnoreCase(
            String source,
            String destination
    );

    // Search by source
    List<Train> findBySourceIgnoreCase(String source);

    // Search by destination
    List<Train> findByDestinationIgnoreCase(String destination);

    // Search train by name
    List<Train> findByTrainNameContainingIgnoreCase(String trainName);

    // Check whether train number already exists
    boolean existsByTrainNumber(String trainNumber);
}