package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/get-all")
    public ResponseEntity getMyBookings(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(bookingService.getMyBookings(user.getId()));
    }

    @PostMapping("add/{sessionId}/{subscriptionId}")
    public ResponseEntity addBooking(@AuthenticationPrincipal User user , @PathVariable Integer sessionId, @PathVariable Integer subscriptionId) {
        bookingService.addBooking(user.getId(),sessionId,subscriptionId);
        return ResponseEntity.status(200).body(new ApiResponse("Book added successfully"));
    }

    @PostMapping("check/{bookingId}")
    public ResponseEntity changeBookStatus(@AuthenticationPrincipal User user, @PathVariable Integer bookingId) {
        bookingService.checkBookState(user.getId(), bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("Status Checked"));
    }

    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity cancelBooking(@AuthenticationPrincipal User user,@PathVariable Integer bookingId) {
        bookingService.cancelPendingBooking(user.getId(),bookingId);
        return ResponseEntity.ok(new ApiResponse("Booking cancelled successfully."));
    }
    
    // Admin endpoint to get all bookings with details
    @GetMapping("/admin/get-all")
    public ResponseEntity getAllBookingsForAdmin(@AuthenticationPrincipal User admin) {
        if (!admin.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        return ResponseEntity.ok(bookingService.getAllBookingsWithDetails());
    }

}
