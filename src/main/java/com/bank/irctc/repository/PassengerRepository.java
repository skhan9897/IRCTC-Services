package com.bank.irctc.repository;

import com.bank.irctc.entity.Passenger;
import com.bank.irctc.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    // Get all passengers of a booking
    List<Passenger> findByBooking(Booking booking);

    // Get passengers by booking ID
    List<Passenger> findByBookingId(Long bookingId);

    // Find passengers by gender
    List<Passenger> findByGender(String gender);

    // Check passenger by booking
    boolean existsByBookingId(Long bookingId);
}