package com.bank.irctc.controller;

import com.bank.irctc.entity.Payment;
import com.bank.irctc.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
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
}