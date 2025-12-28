package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.DTOOut.AdminOverviewStatsDTO;
import com.levelup.levelup_academy.DTOOut.PlayerDetailsDTO;
import com.levelup.levelup_academy.DTOOut.PlayerWithSubscriptionDTO;
import com.levelup.levelup_academy.Model.*;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Repository.BookingRepository;
import com.levelup.levelup_academy.Repository.ParentRepository;
import com.levelup.levelup_academy.Repository.PlayerRepository;
import com.levelup.levelup_academy.Repository.ProRepository;
import com.levelup.levelup_academy.Repository.SubscriptionRepository;
import com.levelup.levelup_academy.Repository.TrainerRepository;
import com.levelup.levelup_academy.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final PlayerRepository playerRepository;
    private final TrainerRepository trainerRepository;
    private final ProRepository proRepository;
    private final ParentRepository parentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final BookingRepository bookingRepository;
    private final AuthRepository authRepository;
    
    // US-ADM-PL-1: Get all players with subscription & payment status
    @GetMapping("/players")
    public ResponseEntity<List<PlayerWithSubscriptionDTO>> getAllPlayersWithSubscription(
            @AuthenticationPrincipal User admin,
            @RequestParam(required = false, defaultValue = "ALL") String status) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(adminService.getAllPlayersWithSubscription(status));
    }
    
    // US-ADM-PL-2: Get detailed player information
    @GetMapping("/players/{playerId}/details")
    public ResponseEntity<PlayerDetailsDTO> getPlayerDetails(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer playerId) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(adminService.getPlayerDetails(playerId));
    }
    
    // US-ADM-GEN-1: Edit any user
    @PatchMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> updateUser(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer userId,
            @RequestBody Map<String, Object> updates) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        User user = authRepository.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        
        // Update fields
        if (updates.containsKey("firstName")) {
            user.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            user.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            // Check email uniqueness
            User existingUser = authRepository.findUserByEmail(email);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return ResponseEntity.status(400).body(new ApiResponse("Email already exists"));
            }
            user.setEmail(email);
        }
        if (updates.containsKey("username")) {
            String username = (String) updates.get("username");
            // Check username uniqueness
            User existingUser = authRepository.findUserByUsername(username);
            if (existingUser != null && !existingUser.getId().equals(userId)) {
                return ResponseEntity.status(400).body(new ApiResponse("Username already exists"));
            }
            user.setUsername(username);
        }
        if (updates.containsKey("role") && "ADMIN".equals(admin.getRole())) {
            String role = (String) updates.get("role");
            if (!isValidRole(role)) {
                return ResponseEntity.status(400).body(new ApiResponse("Invalid role"));
            }
            user.setRole(role);
        }
        
        authRepository.save(user);
        return ResponseEntity.ok(new ApiResponse("User updated successfully"));
    }
    
    // US-ADM-GEN-2: Delete/Deactivate user
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer userId) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        if (userId.equals(admin.getId())) {
            return ResponseEntity.status(400).body(new ApiResponse("Cannot delete your own account"));
        }
        
        User user = authRepository.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        
        // Soft delete - we'll add a status field later, for now just delete
        // TODO: Implement soft delete with status field
        authRepository.delete(user);
        
        return ResponseEntity.ok(new ApiResponse("User deleted successfully"));
    }
    
    // US-ADM-GEN-3: Ban user
    @PatchMapping("/users/{userId}/ban")
    public ResponseEntity<ApiResponse> banUser(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer userId) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        if (userId.equals(admin.getId())) {
            return ResponseEntity.status(400).body(new ApiResponse("Cannot ban your own account"));
        }
        
        User user = authRepository.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        
        user.setEnabled(false);
        authRepository.save(user);
        
        return ResponseEntity.ok(new ApiResponse("User banned successfully"));
    }
    
    // US-ADM-GEN-4: Unban user
    @PatchMapping("/users/{userId}/unban")
    public ResponseEntity<ApiResponse> unbanUser(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer userId) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        User user = authRepository.findUserById(userId);
        if (user == null) {
            return ResponseEntity.status(404).body(new ApiResponse("User not found"));
        }
        
        user.setEnabled(true);
        authRepository.save(user);
        
        return ResponseEntity.ok(new ApiResponse("User unbanned successfully"));
    }
    
    // US-ADM-GEN-5: Cancel subscription
    @PatchMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<ApiResponse> updateSubscription(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer subscriptionId,
            @RequestBody Map<String, Object> updates) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        Subscription subscription = subscriptionRepository.findSubscriptionById(subscriptionId);
        if (subscription == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Subscription not found"));
        }
        
        if (updates.containsKey("status")) {
            String status = (String) updates.get("status");
            subscription.setStatus(status);
        }
        if (updates.containsKey("packageType")) {
            subscription.setPackageType((String) updates.get("packageType"));
        }
        
        subscriptionRepository.save(subscription);
        return ResponseEntity.ok(new ApiResponse("Subscription updated successfully"));
    }
    
    // US-ADM-GEN-6: Cancel booking
    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<ApiResponse> cancelBooking(
            @AuthenticationPrincipal User admin,
            @PathVariable Integer bookingId) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            return ResponseEntity.status(404).body(new ApiResponse("Booking not found"));
        }
        
        // Update session available seats
        Session session = booking.getSession();
        if (session != null && !"CANCELLED".equals(booking.getStatus())) {
            session.setAvailableSets(session.getAvailableSets() + 1);
        }
        
        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
        
        return ResponseEntity.ok(new ApiResponse("Booking cancelled successfully"));
    }
    
    // US-ADM-GEN-7: Get overview statistics
    @GetMapping("/stats/overview")
    public ResponseEntity<AdminOverviewStatsDTO> getOverviewStats(
            @AuthenticationPrincipal User admin) {
        
        if (!"ADMIN".equals(admin.getRole())) {
            return ResponseEntity.status(403).build();
        }
        
        long totalPlayers = playerRepository.count();
        long totalTrainers = trainerRepository.count();
        long totalProPlayers = proRepository.count();
        long totalParents = parentRepository.count();
        
        List<Subscription> activeSubscriptions = subscriptionRepository.findAll().stream()
            .filter(sub -> "ACTIVE".equals(sub.getStatus()))
            .collect(Collectors.toList());
        
        long activeSubscriptionsCount = activeSubscriptions.size();
        
        // Calculate total revenue (sum of all active subscription prices)
        double totalRevenue = activeSubscriptions.stream()
            .mapToDouble(sub -> sub.getPrice() != null ? sub.getPrice().doubleValue() : 0.0)
            .sum();
        
        AdminOverviewStatsDTO stats = new AdminOverviewStatsDTO(
            (int) totalPlayers,
            (int) totalTrainers,
            (int) totalProPlayers,
            (int) totalParents,
            (int) activeSubscriptionsCount,
            totalRevenue,
            "SAR"
        );
        
        return ResponseEntity.ok(stats);
    }
    
    private boolean isValidRole(String role) {
        return role != null && (role.equals("ADMIN") || role.equals("MODERATOR") || 
                               role.equals("PLAYER") || role.equals("PRO") || 
                               role.equals("TRAINER") || role.equals("PARENTS"));
    }
}

