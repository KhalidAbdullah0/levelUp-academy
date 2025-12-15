package com.levelup.levelup_academy.Controller;


import com.levelup.levelup_academy.Api.ApiResponse;
import com.levelup.levelup_academy.Model.User;
import com.levelup.levelup_academy.Repository.AuthRepository;
import com.levelup.levelup_academy.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class
UserController {

    private final UserService userService;
    private final AuthRepository authRepository;

    @PostMapping("generate/{adminId}/{moderatorId}")
    public ResponseEntity generateModeratorLogin(@PathVariable Integer adminId, @PathVariable Integer moderatorId){
        userService.generateModeratorLogin(adminId, moderatorId);
        return ResponseEntity.status(200).body(new ApiResponse("Username and Password generated for moderator"));
    }

    @GetMapping("/get-all")
    public ResponseEntity getAllSubscriptions(@AuthenticationPrincipal User adminId) {
        return ResponseEntity.status(200).body(userService.getAllSubscriptions(adminId.getId()));
    }
    
    @GetMapping("/get-all-subscriptions-with-users")
    public ResponseEntity getAllSubscriptionsWithUsers(@AuthenticationPrincipal User admin) {
        if (!admin.getRole().equals("ADMIN")) {
            return ResponseEntity.status(403).body(new ApiResponse("Access denied"));
        }
        return ResponseEntity.status(200).body(userService.getAllSubscriptionsWithUser(admin.getId()));
    }

    @GetMapping("/get-role")
    public ResponseEntity<Map<String, String>> getRole(@AuthenticationPrincipal User user) {
        Map<String, String> result = new HashMap<>();
        result.put("role", user.getRole());
        return ResponseEntity.ok(result);
    }



}
