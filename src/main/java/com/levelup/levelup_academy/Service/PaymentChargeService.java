package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTO.PaymentChargeRequest;
import com.levelup.levelup_academy.DTOOut.PaymentChargeResponse;
import com.levelup.levelup_academy.Model.Booking;
import com.levelup.levelup_academy.Model.PaymentRequest;
import com.levelup.levelup_academy.Model.Session;
import com.levelup.levelup_academy.Model.Subscription;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Repository.BookingRepository;
import com.levelup.levelup_academy.Repository.SessionRepository;
import com.levelup.levelup_academy.Repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentChargeService {
    
    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;
    private final SessionRepository sessionRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final AuthRepository authRepository;
    
    public PaymentChargeResponse chargePayment(Integer userId, PaymentChargeRequest request) {
        // Validate user
        User user = authRepository.findUserById(userId);
        if (user == null) throw new ApiException("User not found");
        
        // Validate subscription
        Subscription subscription = subscriptionRepository.findSubscriptionById(request.getSubscriptionId());
        if (subscription == null) throw new ApiException("Subscription not found");
        if (!subscription.getUser().getId().equals(userId)) {
            throw new ApiException("Subscription does not belong to this user");
        }
        if (!subscription.getPrice().equals(request.getAmount().intValue())) {
            throw new ApiException("Payment amount does not match subscription price");
        }
        
        // Validate session
        Session session = sessionRepository.findSessionById(request.getSessionId());
        if (session == null) throw new ApiException("Session not found");
        if (session.getAvailableSets() <= 0) {
            throw new ApiException("No available seats for this session");
        }
        
        // Check for duplicate booking at same time
        var existingBookings = bookingRepository.findByUserId(userId);
        for (Booking b : existingBookings) {
            if (b.getSession().getId().equals(request.getSessionId()) && 
                !b.getStatus().equals("CANCELLED")) {
                throw new ApiException("You already have a booking for this session");
            }
        }
        
        // Process payment via Moyasar
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setName(request.getCard().getCardHolderName());
        paymentRequest.setNumber(request.getCard().getCardNumber().replaceAll("\\s+", ""));
        paymentRequest.setMonth(request.getCard().getExpiryMonth());
        paymentRequest.setYear(request.getCard().getExpiryYear());
        paymentRequest.setCvc(Integer.parseInt(request.getCard().getCvv()));
        paymentRequest.setAmount(request.getAmount());
        paymentRequest.setCurrency(request.getCurrency() != null ? request.getCurrency() : "SAR");
        paymentRequest.setDescription("Subscription: " + subscription.getPackageType() + " - Session: " + session.getName());
        paymentRequest.setCallbackUrl("http://localhost:8080/api/v1/payments/callback");
        
        ResponseEntity<String> paymentResponse = paymentService.processPayment(paymentRequest);
        
        // Check payment result
        boolean paymentSuccess = paymentResponse.getStatusCode().is2xxSuccessful();
        
        Integer paymentId = null;
        try {
            // Try to extract payment ID from Moyasar response
            String responseBody = paymentResponse.getBody();
            if (responseBody != null && responseBody.contains("\"id\"")) {
                // Parse JSON to get payment ID (simplified - you may need proper JSON parsing)
                int idStart = responseBody.indexOf("\"id\":") + 5;
                int idEnd = responseBody.indexOf(",", idStart);
                if (idEnd == -1) idEnd = responseBody.indexOf("}", idStart);
                if (idStart > 4 && idEnd > idStart) {
                    String idStr = responseBody.substring(idStart, idEnd).trim().replace("\"", "");
                    paymentId = Integer.parseInt(idStr);
                }
            }
        } catch (Exception e) {
            // If we can't parse payment ID, use a placeholder
            paymentId = 0;
        }
        
        if (!paymentSuccess) {
            return new PaymentChargeResponse(
                paymentId,
                "FAILED",
                request.getSubscriptionId(),
                request.getSessionId(),
                null
            );
        }
        
        // Payment successful - create booking
        Booking booking = new Booking();
        booking.setBookDate(LocalDateTime.now());
        booking.setUser(user);
        booking.setSession(session);
        booking.setSubscription(subscription);
        booking.setStatus("CONFIRMED");
        
        // Decrease available seats
        session.setAvailableSets(session.getAvailableSets() - 1);
        
        // Decrease subscription session count
        if (subscription.getSessionCount() > 0) {
            subscription.setSessionCount(subscription.getSessionCount() - 1);
        }
        
        // Save all changes
        sessionRepository.save(session);
        subscriptionRepository.save(subscription);
        Booking savedBooking = bookingRepository.save(booking);
        
        return new PaymentChargeResponse(
            paymentId,
            "SUCCESS",
            request.getSubscriptionId(),
            request.getSessionId(),
            savedBooking.getId()
        );
    }
}

