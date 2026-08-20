package com.bank.irctc.controller;

import com.bank.irctc.entity.Booking;
import com.bank.irctc.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // =========================
    // CREATE BOOKING
    // =========================
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody Booking booking) {

        Booking savedBooking = bookingService.createBooking(booking);

        return ResponseEntity.ok(savedBooking);
    }

    // =========================
    // GET BOOKING BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @PathVariable Long id) {

        Booking booking = bookingService.getBookingById(id);

        return ResponseEntity.ok(booking);
    }

    // =========================
    // GET USER BOOKINGS
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(
            @PathVariable Long userId) {

        List<Booking> bookings =
                bookingService.getBookingsByUser(userId);

        return ResponseEntity.ok(bookings);
    }

    // =========================
    // CANCEL BOOKING
    // =========================
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id) {

        Booking cancelledBooking =
                bookingService.cancelBooking(id);

        return ResponseEntity.ok(cancelledBooking);
    }
}