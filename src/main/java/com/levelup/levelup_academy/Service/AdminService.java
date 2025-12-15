package com.levelup.levelup_academy.Service;

import com.levelup.levelup_academy.Api.ApiException;
import com.levelup.levelup_academy.DTOOut.PlayerDetailsDTO;
import com.levelup.levelup_academy.DTOOut.PlayerWithSubscriptionDTO;
import com.levelup.levelup_academy.Model.*;
import com.levelup.levelup_academy.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final PlayerRepository playerRepository;
    private final AuthRepository authRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BookingRepository bookingRepository;
    
    // Get all players with subscription and payment status
    public List<PlayerWithSubscriptionDTO> getAllPlayersWithSubscription(String status) {
        List<Player> players = playerRepository.findAll();
        List<PlayerWithSubscriptionDTO> result = new ArrayList<>();
        
        for (Player player : players) {
            User user = player.getUser();
            if (user == null) continue;
            
            // Get latest subscription
            List<Subscription> subscriptions = subscriptionRepository.findAll().stream()
                .filter(sub -> sub.getUser() != null && sub.getUser().getId().equals(user.getId()))
                .sorted((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .collect(Collectors.toList());
            
            Subscription latestSubscription = subscriptions.isEmpty() ? null : subscriptions.get(0);
            
            // Determine current plan
            String currentPlan = "NONE";
            String subscriptionStatus = "NONE";
            String paymentStatus = "NONE";
            Integer totalClasses = 0;
            Integer usedClasses = 0;
            
            if (latestSubscription != null) {
                currentPlan = latestSubscription.getPackageType();
                subscriptionStatus = latestSubscription.getStatus();
                
                // Determine payment status based on subscription status
                if ("ACTIVE".equals(subscriptionStatus)) {
                    paymentStatus = "PAID";
                } else if ("PENDING_PAYMENT".equals(subscriptionStatus)) {
                    paymentStatus = "UNPAID";
                } else if ("PENDING".equals(subscriptionStatus)) {
                    paymentStatus = "UNPAID";
                } else {
                    paymentStatus = "NONE";
                }
                
                totalClasses = latestSubscription.getSessionCount();
                
                // Count used classes (bookings for this subscription)
                List<Booking> bookings = bookingRepository.findAll().stream()
                    .filter(b -> b.getSubscription() != null && 
                                b.getSubscription().getId().equals(latestSubscription.getId()) &&
                                !"CANCELLED".equals(b.getStatus()))
                    .collect(Collectors.toList());
                
                usedClasses = bookings.size();
            }
            
            PlayerWithSubscriptionDTO dto = new PlayerWithSubscriptionDTO(
                player.getId(),
                user.getId(),
                user.getUsername(),
                user.getFirstName() + " " + user.getLastName(),
                user.getEmail(),
                user.getRegistration(),
                currentPlan,
                subscriptionStatus,
                paymentStatus,
                totalClasses,
                usedClasses
            );
            
            // Apply filters
            if (status == null || "ALL".equals(status)) {
                result.add(dto);
            } else if ("SUBSCRIBED".equals(status) && !"NONE".equals(currentPlan)) {
                result.add(dto);
            } else if ("UNPAID".equals(status) && ("UNPAID".equals(paymentStatus) || "PENDING_PAYMENT".equals(subscriptionStatus))) {
                result.add(dto);
            } else if ("ACTIVE_PAID".equals(status) && "ACTIVE".equals(subscriptionStatus) && "PAID".equals(paymentStatus)) {
                result.add(dto);
            }
        }
        
        return result;
    }
    
    // Get detailed player information
    public PlayerDetailsDTO getPlayerDetails(Integer playerId) {
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ApiException("Player not found"));
        
        User user = player.getUser();
        if (user == null) {
            throw new ApiException("User not found for this player");
        }
        
        // Player info
        PlayerDetailsDTO.PlayerInfo playerInfo = new PlayerDetailsDTO.PlayerInfo(
            player.getId(),
            user.getId(),
            user.getUsername(),
            user.getFirstName() + " " + user.getLastName(),
            user.getEmail(),
            user.getRegistration(),
            "ACTIVE" // TODO: Add status field to User model
        );
        
        // Subscriptions
        List<Subscription> subscriptions = subscriptionRepository.findAll().stream()
            .filter(sub -> sub.getUser() != null && sub.getUser().getId().equals(user.getId()))
            .collect(Collectors.toList());
        
        List<PlayerDetailsDTO.SubscriptionInfo> subscriptionInfos = subscriptions.stream()
            .map(sub -> new PlayerDetailsDTO.SubscriptionInfo(
                sub.getId(),
                sub.getPackageType(),
                sub.getSessionCount(),
                sub.getPrice(),
                "SAR",
                sub.getStatus(),
                sub.getStartDate(),
                sub.getEndDate()
            ))
            .collect(Collectors.toList());
        
        // Payments (from bookings - since we don't have a Payment entity, we use subscription status)
        List<PlayerDetailsDTO.PaymentInfo> paymentInfos = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if ("ACTIVE".equals(sub.getStatus()) || "PENDING_PAYMENT".equals(sub.getStatus())) {
                // Find booking for this subscription to get payment date
                List<Booking> bookings = bookingRepository.findAll().stream()
                    .filter(b -> b.getSubscription() != null && b.getSubscription().getId().equals(sub.getId()))
                    .collect(Collectors.toList());
                
                LocalDateTime paidAt = bookings.isEmpty() ? LocalDateTime.now() : bookings.get(0).getBookDate();
                
                paymentInfos.add(new PlayerDetailsDTO.PaymentInfo(
                    sub.getId(), // Using subscription ID as payment ID
                    sub.getId(),
                    sub.getPrice().doubleValue(),
                    "SAR",
                    "ACTIVE".equals(sub.getStatus()) ? "SUCCESS" : "PENDING",
                    paidAt
                ));
            }
        }
        
        // Bookings
        List<Booking> bookings = bookingRepository.findAll().stream()
            .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
            .collect(Collectors.toList());
        
        List<PlayerDetailsDTO.BookingInfo> bookingInfos = bookings.stream()
            .map(booking -> {
                Session session = booking.getSession();
                Subscription subscription = booking.getSubscription();
                Trainer trainer = session != null ? session.getTrainer() : null;
                
                String trainerName = trainer != null && trainer.getUser() != null 
                    ? trainer.getUser().getFirstName() + " " + trainer.getUser().getLastName()
                    : "N/A";
                
                String plan = subscription != null ? subscription.getPackageType() : "N/A";
                
                // Determine booking status
                String bookingStatus = "UPCOMING";
                if ("CANCELLED".equals(booking.getStatus())) {
                    bookingStatus = "CANCELLED";
                } else if (session != null && session.getStartDate() != null && 
                          LocalDateTime.now().isAfter(session.getStartDate())) {
                    bookingStatus = "COMPLETED";
                }
                
                return new PlayerDetailsDTO.BookingInfo(
                    booking.getId(),
                    session != null ? session.getId() : null,
                    session != null ? session.getStartDate() : null,
                    session != null ? session.getEndDate() : null,
                    trainerName,
                    plan,
                    bookingStatus
                );
            })
            .collect(Collectors.toList());
        
        return new PlayerDetailsDTO(playerInfo, subscriptionInfos, paymentInfos, bookingInfos);
    }
}

