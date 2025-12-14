package com.levelup.levelup_academy.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {
    
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        // Return 204 No Content instead of 400 Bad Request
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

