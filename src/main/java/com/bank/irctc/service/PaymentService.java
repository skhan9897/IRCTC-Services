package com.bank.irctc.service;

import com.bank.irctc.entity.Booking;
import com.bank.irctc.entity.Payment;
import com.bank.irctc.repository.BookingRepository;
import com.bank.irctc.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    // =========================
    // CREATE PAYMENT
    // =========================
    public Payment createPayment(Payment payment) {

        if (payment.getBooking() == null ||
                payment.getBooking().getId() == null) {

            throw new RuntimeException(
                    "Booking ID is required"
            );
        }

        Long bookingId = payment.getBooking().getId();

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + bookingId
                        )
                );

        // Check existing payment
        if (paymentRepository.existsByBookingId(bookingId)) {
            throw new RuntimeException(
                    "Payment already exists for this booking"
            );
        }

        // Set booking
        payment.setBooking(booking);

        // Generate transaction ID
        String transactionId =
                "TXN-" + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 12)
                        .toUpperCase();

        payment.setTransactionId(transactionId);

        // Payment time
        payment.setPaymentTime(LocalDateTime.now());

        /*
         * Demo payment processing.
         * Real payment gateway integration can be
         * added later.
         */
        payment.setPaymentStatus("SUCCESS");

        // Payment success → Booking confirmed
        booking.setBookingStatus("CONFIRMED");

        bookingRepository.save(booking);

        return paymentRepository.save(payment);
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    public Payment getPaymentById(Long id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with ID: " + id
                        )
                );
    }

    // =========================
    // GET PAYMENT STATUS
    // =========================
    public String getPaymentStatus(Long id) {

        Payment payment = getPaymentById(id);

        return payment.getPaymentStatus();
    }
}