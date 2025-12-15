package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.DTO.PaymentChargeRequest;
import com.levelup.levelup_academy.DTOOut.PaymentChargeResponse;
import com.levelup.levelup_academy.Model.PaymentRequest;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.PaymentChargeService;
import com.levelup.levelup_academy.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentChargeService paymentChargeService;

    @PostMapping("/card")
    public ResponseEntity<ResponseEntity<String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok().body(paymentService.processPayment(paymentRequest));
    }

    @PostMapping("/charge")
    public ResponseEntity<PaymentChargeResponse> chargePayment(
            @AuthenticationPrincipal User user,
            @RequestBody PaymentChargeRequest request) {
        PaymentChargeResponse response = paymentChargeService.chargePayment(user.getId(), request);
        if ("FAILED".equals(response.getStatus())) {
            return ResponseEntity.status(402).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-status/{id}")
    public ResponseEntity getPaymentStatus(@PathVariable String id) {
        return ResponseEntity.ok().body(paymentService.getPaymentStatus(id));
    }

    @GetMapping("callback")
    public ResponseEntity getCallback() {
        return ResponseEntity.ok().body(paymentService.callback());
    }
}
