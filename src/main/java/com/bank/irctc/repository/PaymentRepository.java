package com.bank.irctc.repository;

import com.bank.irctc.entity.Payment;
import com.bank.irctc.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by transaction ID
    Optional<Payment> findByTransactionId(String transactionId);

    // Find payment by booking
    Optional<Payment> findByBooking(Booking booking);

    // Find payment by booking ID
    Optional<Payment> findByBookingId(Long bookingId);

    // Find payments by status
    List<Payment> findByPaymentStatus(String paymentStatus);

    // Check transaction ID
    boolean existsByTransactionId(String transactionId);

    // Check whether payment exists for booking
    boolean existsByBookingId(Long bookingId);
}