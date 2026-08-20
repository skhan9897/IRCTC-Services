package com.bank.irctc.service;

import com.bank.irctc.entity.Booking;
import com.bank.irctc.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // =========================
    // CREATE BOOKING
    // =========================
    public Booking createBooking(Booking booking) {

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

        return String.valueOf(
                Math.abs(
                        UUID.randomUUID()
                                .toString()
                                .hashCode()
                )
        ).substring(0, 10);
    }
}