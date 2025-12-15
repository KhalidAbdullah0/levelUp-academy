package com.levelup.levelup_academy.Controller;

import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.Model.Session;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    //GET - Accessible by all authenticated users (MODERATOR, ADMIN, PLAYER)
    @GetMapping("/get")
    public ResponseEntity getAllSession(){
        return ResponseEntity.status(200).body(sessionService.getAllClasses());
    }

    //ADD
    @PostMapping("/add/{trainerId}/{gameId}")
    public ResponseEntity addSession(@AuthenticationPrincipal User user, @RequestBody @Valid Session session, @PathVariable Integer trainerId, @PathVariable Integer gameId){
        // Check if user is ADMIN or MODERATOR
        if (user.getRole().equals("ADMIN")) {
            sessionService.addClassForAdmin(session, trainerId, gameId);
        } else if (user.getRole().equals("MODERATOR")) {
            if (user.getModerator() == null) {
                return ResponseEntity.status(403).body(new ApiResponse("Moderator not found"));
            }
            sessionService.addClass(user.getModerator().getId(), session, trainerId, gameId);
        } else {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        return ResponseEntity.status(200).body(new ApiResponse("Session Added"));
    }



    //update
    @PutMapping("/update/{sessionId}")
    public ResponseEntity updateSession(@AuthenticationPrincipal User moderator ,@PathVariable Integer sessionId, @RequestBody @Valid Session session){
        sessionService.updateSession(moderator.getId(),session,sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Session Updated"));
    }

    //delete
    @DeleteMapping("/del/{sessionId}")
    public ResponseEntity deleteSession(@AuthenticationPrincipal User moderator,@PathVariable Integer sessionId){
        sessionService.deleteSession(moderator.getId(), sessionId);
        return ResponseEntity.status(200).body(new ApiResponse("Session Deleted"));
    }

    @GetMapping("/notify-start/{sessionId}")
    public ResponseEntity notifyUsersOfSessionStart(@AuthenticationPrincipal User trainer, @PathVariable Integer sessionId) {
        sessionService.notifyUsersIfSessionStarting(trainer.getId(),sessionId);
        return ResponseEntity.ok(new ApiResponse("Emails sent to all booked users for session ID: " + sessionId));
    }

    @PutMapping("/change-session/{trainerId}/{newSessionId}")
    public ResponseEntity changeTrainerSession(@AuthenticationPrincipal User moderator,
            @PathVariable Integer trainerId,
            @PathVariable Integer newSessionId) {
        sessionService.changeTrainerSession(moderator.getId(), trainerId, newSessionId);
        return ResponseEntity.ok(new ApiResponse("Trainer session changed successfully."));
    }



}
