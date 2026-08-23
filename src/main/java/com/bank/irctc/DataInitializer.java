package com.bank.irctc;

import com.bank.irctc.entity.Train;
import com.bank.irctc.repository.TrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TrainRepository trainRepository;

    public DataInitializer(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (trainRepository.count() == 0) {
            Train train1 = new Train();
            train1.setTrainNumber("12230");
            train1.setTrainName("Lucknow Mail");
            train1.setSource("Lucknow");
            train1.setDestination("New Delhi");
            train1.setDepartureTime("22:00");
            train1.setArrivalTime("06:45");
            train1.setTotalSeats(500);
            train1.setAvailableSeats(450);
            train1.setSleeperFare(350.0);
            train1.setAc3Fare(950.0);
            train1.setAc2Fare(1350.0);
            train1.setAc1Fare(2200.0);

            Train train2 = new Train();
            train2.setTrainNumber("12435");
            train2.setTrainName("Dibrugarh Rajdhani");
            train2.setSource("New Delhi");
            train2.setDestination("Dibrugarh");
            train2.setDepartureTime("11:25");
            train2.setArrivalTime("05:30");
            train2.setTotalSeats(300);
            train2.setAvailableSeats(120);
            train2.setAc3Fare(2500.0);
            train2.setAc2Fare(3500.0);
            train2.setAc1Fare(5000.0);

            Train train3 = new Train();
            train3.setTrainNumber("12030");
            train3.setTrainName("Amritsar Shatabdi");
            train3.setSource("New Delhi");
            train3.setDestination("Amritsar");
            train3.setDepartureTime("07:20");
            train3.setArrivalTime("13:20");
            train3.setTotalSeats(400);
            train3.setAvailableSeats(200);
            train3.setAc3Fare(1200.0);
            train3.setAc2Fare(1800.0);

            Train train4 = new Train();
            train4.setTrainNumber("12952");
            train4.setTrainName("Mumbai Rajdhani");
            train4.setSource("New Delhi");
            train4.setDestination("Mumbai");
            train4.setDepartureTime("16:25");
            train4.setArrivalTime("08:15");
            train4.setTotalSeats(350);
            train4.setAvailableSeats(150);
            train4.setAc3Fare(2800.0);
            train4.setAc2Fare(3800.0);
            train4.setAc1Fare(5200.0);
            
            Train train5 = new Train();
            train5.setTrainNumber("14316");
            train5.setTrainName("Bareilly Express");
            train5.setSource("New Delhi");
            train5.setDestination("Bareilly");
            train5.setDepartureTime("16:35");
            train5.setArrivalTime("21:50");
            train5.setTotalSeats(600);
            train5.setAvailableSeats(500);
            train5.setSleeperFare(180.0);
            train5.setAc3Fare(500.0);
            train5.setAc2Fare(750.0);

            trainRepository.saveAll(Arrays.asList(train1, train2, train3, train4, train5));
            System.out.println("Sample trains added to the database.");
        }
    }
}
