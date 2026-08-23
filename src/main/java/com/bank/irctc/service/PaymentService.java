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

        Long bookingId = payment.getBookingId();

        if (payment.getBooking() != null && payment.getBooking().getId() != null) {
            bookingId = payment.getBooking().getId();
        }

        if (bookingId == null) {
            throw new RuntimeException(
                    "Booking ID is required"
            );
        }

        final Long resolvedBookingId = bookingId;

        Booking booking = bookingRepository.findById(resolvedBookingId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with ID: " + resolvedBookingId
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
        payment.setBookingId(bookingId);

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

        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            payment.setAmount(booking.getTotalFare());
        }

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