package com.bank.irctc.controller;

import com.bank.irctc.entity.Payment;
import com.bank.irctc.service.PaymentService;
import com.bank.irctc.repository.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentService paymentService,
                            PaymentRepository paymentRepository) {
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    // =========================
    // CREATE PAYMENT
    // =========================
    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @RequestBody Payment payment) {

        Payment savedPayment = paymentService.createPayment(payment);

        return ResponseEntity.ok(savedPayment);
    }

    // =========================
    // GET PAYMENT BY ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(
            @PathVariable Long id) {

        Payment payment = paymentService.getPaymentById(id);

        return ResponseEntity.ok(payment);
    }

    // =========================
    // PAYMENT STATUS
    // =========================
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getPaymentStatus(
            @PathVariable Long id) {

        String status = paymentService.getPaymentStatus(id);

        return ResponseEntity.ok(status);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBookingId(
            @PathVariable Long bookingId) {

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking ID: " + bookingId));

        return ResponseEntity.ok(payment);
    }
}