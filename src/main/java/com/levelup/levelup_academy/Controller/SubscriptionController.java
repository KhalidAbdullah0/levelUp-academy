package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.DTOOut.SubscriptionResponse;
import com.levelup.levelup_academy.Model.PaymentRequest;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    // Create subscription without payment (US-PL-4)
    @PostMapping("/basic")
    public ResponseEntity<SubscriptionResponse> createBasicSubscription(@AuthenticationPrincipal User user) {
        SubscriptionResponse response = subscriptionService.createSubscription(user.getId(), "BASIC");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/standard")
    public ResponseEntity<SubscriptionResponse> createStandardSubscription(@AuthenticationPrincipal User user) {
        SubscriptionResponse response = subscriptionService.createSubscription(user.getId(), "STANDARD");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/premium")
    public ResponseEntity<SubscriptionResponse> createPremiumSubscription(@AuthenticationPrincipal User user) {
        SubscriptionResponse response = subscriptionService.createSubscription(user.getId(), "PREMIUM");
        return ResponseEntity.ok(response);
    }
    
    // Legacy endpoints with payment (kept for backward compatibility)
    @PostMapping("/basic/with-payment")
    public ResponseEntity basicSubscriptionWithPayment(@AuthenticationPrincipal User user, @RequestBody PaymentRequest paymentRequest) {
        return subscriptionService.basicSubscription(user.getId(), paymentRequest);
    }

    @PostMapping("/standard/with-payment")
    public ResponseEntity standardSubscriptionWithPayment(@AuthenticationPrincipal User user, @RequestBody PaymentRequest paymentRequest) {
        return subscriptionService.standardSubscription(user.getId(), paymentRequest);
    }

    @PostMapping("/premium/with-payment")
    public ResponseEntity premiumSubscriptionWithPayment(@AuthenticationPrincipal User user, @RequestBody PaymentRequest paymentRequest) {
        return subscriptionService.premiumSubscription(user.getId(), paymentRequest);
    }

//    @PostMapping("/subscribe/{userId}")
//    public ResponseEntity subscribeWithPayment(@PathVariable Integer userId,@RequestParam String packageType, @RequestBody PaymentRequest paymentRequest) {
//        return subscriptionService.subscribeWithPayment(userId, packageType, paymentRequest);
//    }
//    @GetMapping("/get-subs")
//    public ResponseEntity gatAllSubs(@AuthenticationPrincipal User user){
//        return ResponseEntity.status(200).body(subscriptionService.gatAllSubs(user.getId()));
//    }


}
