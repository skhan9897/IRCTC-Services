package com.bank.irctc.repository;

import com.bank.irctc.entity.Booking;
import com.bank.irctc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find booking by PNR
    Optional<Booking> findByPnr(String pnr);

    // Get all bookings of a user
    List<Booking> findByUser(User user);

    // Get bookings by user ID
    List<Booking> findByUserId(Long userId);

    // Find bookings by status
    List<Booking> findByBookingStatus(String bookingStatus);

    // Check whether PNR already exists
    boolean existsByPnr(String pnr);
}