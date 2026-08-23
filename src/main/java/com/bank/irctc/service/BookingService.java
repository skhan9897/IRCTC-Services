package com.bank.irctc.service;

import com.bank.irctc.entity.*;
import com.bank.irctc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final TrainScheduleRepository scheduleRepository;

    public BookingService(BookingRepository bookingRepository,
                         UserRepository userRepository,
                         TrainRepository trainRepository,
                         TrainScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // =========================
    // CREATE BOOKING
    // =========================
    @Transactional
    public Booking createBooking(Booking booking) {

        if (booking == null) {
            throw new RuntimeException("Booking details are required");
        }

        Long userId = booking.getUserId();
        Long trainId = booking.getTrainId();

        if (booking.getUser() == null && userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
            booking.setUser(user);
        }

        if (booking.getTrain() == null && trainId != null) {
            Train train = trainRepository.findById(trainId)
                    .orElseThrow(() -> new RuntimeException("Train not found with ID: " + trainId));
            booking.setTrain(train);
        }

        if (booking.getUser() == null) {
            throw new RuntimeException("User is required for booking");
        }

        if (booking.getTrain() == null) {
            throw new RuntimeException("Train is required for booking");
        }

        Train train = booking.getTrain();

        if (booking.getFromStation() == null || booking.getFromStation().isBlank()) {
            booking.setFromStation(train.getSource());
        }

        if (booking.getToStation() == null || booking.getToStation().isBlank()) {
            booking.setToStation(train.getDestination());
        }

        // Calculate Total Fare
        if (booking.getClassType() == null) {
            booking.setClassType("SLEEPER");
        }

        double baseFare = 0.0;
        switch (booking.getClassType().toUpperCase()) {
            case "AC1": baseFare = train.getAc1Fare() != null ? train.getAc1Fare() : 1000.0; break;
            case "AC2": baseFare = train.getAc2Fare() != null ? train.getAc2Fare() : 800.0; break;
            case "AC3": baseFare = train.getAc3Fare() != null ? train.getAc3Fare() : 600.0; break;
            default: baseFare = train.getSleeperFare() != null ? train.getSleeperFare() : 300.0; break;
        }

        int passengerCount = booking.getPassengers() != null ? booking.getPassengers().size() : 1;
        if (passengerCount == 0) passengerCount = 1;

        booking.setTotalFare(baseFare * passengerCount);

        // Update Seats in Schedule if journey date matches
        if (booking.getJourneyDate() != null) {
            List<TrainSchedule> schedules = scheduleRepository.findByTrainIdAndJourneyDate(train.getId(), booking.getJourneyDate());
            if (!schedules.isEmpty()) {
                TrainSchedule schedule = schedules.get(0);
                if (schedule.getAvailableSeats() < passengerCount) {
                    throw new RuntimeException("Not enough seats available for this date");
                }
                schedule.setAvailableSeats(schedule.getAvailableSeats() - passengerCount);
                scheduleRepository.save(schedule);
            } else {
                // Fallback to Train entity seats if no schedule found
                if (train.getAvailableSeats() != null && train.getAvailableSeats() < passengerCount) {
                     throw new RuntimeException("Not enough seats available");
                }
                if (train.getAvailableSeats() != null) {
                    train.setAvailableSeats(train.getAvailableSeats() - passengerCount);
                    trainRepository.save(train);
                }
            }
        }

        // Generate unique PNR
        String pnr;
        do {
            pnr = generatePNR();
        } while (bookingRepository.existsByPnr(pnr));

        booking.setPnr(pnr);

        // Default booking status
        booking.setBookingStatus("PENDING");

        // Booking time
        booking.setBookingTime(LocalDateTime.now());

        // Ensure passengers are linked and assign dummy seats
        if (booking.getPassengers() != null) {
            int seatCounter = 1;
            for (Passenger p : booking.getPassengers()) {
                p.setBooking(booking);
                if (p.getSeatNumber() == null || p.getSeatNumber().isBlank()) {
                    p.setSeatNumber("S" + seatCounter++);
                }
                if (p.getCoach() == null || p.getCoach().isBlank()) {
                    p.setCoach("S1");
                }
            }
        }

        return bookingRepository.save(booking);
    }

    // =========================
    // GET BOOKING BY ID
    // =========================
    public Booking getBookingById(Long id) {

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + id
                        )
                );
    }

    // =========================
    // GET USER BOOKINGS
    // =========================
    public List<Booking> getBookingsByUser(Long userId) {

        return bookingRepository.findByUserId(userId);
    }

    // =========================
    // CANCEL BOOKING
    // =========================
    public Booking cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        if ("CANCELLED".equalsIgnoreCase(
                booking.getBookingStatus())) {

            throw new RuntimeException(
                    "Booking is already cancelled"
            );
        }

        booking.setBookingStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    // =========================
    // GENERATE PNR
    // =========================
    private String generatePNR() {
        long code = Math.abs(UUID.randomUUID().hashCode());
        return String.format("%010d", code);
    }
}