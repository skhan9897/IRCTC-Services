package com.bank.irctc.service;

import com.bank.irctc.entity.Booking;
import com.bank.irctc.entity.Train;
import com.bank.irctc.entity.User;
import com.bank.irctc.repository.BookingRepository;
import com.bank.irctc.repository.TrainRepository;
import com.bank.irctc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;

    public BookingService(BookingRepository bookingRepository,
                         UserRepository userRepository,
                         TrainRepository trainRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.trainRepository = trainRepository;
    }

    // =========================
    // CREATE BOOKING
    // =========================
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

        if (booking.getFromStation() == null || booking.getFromStation().isBlank()) {
            booking.setFromStation(booking.getTrain().getSource());
        }

        if (booking.getToStation() == null || booking.getToStation().isBlank()) {
            booking.setToStation(booking.getTrain().getDestination());
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