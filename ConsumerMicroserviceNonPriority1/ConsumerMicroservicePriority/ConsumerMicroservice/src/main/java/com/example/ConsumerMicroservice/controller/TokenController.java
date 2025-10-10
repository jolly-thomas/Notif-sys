package com.example.ConsumerMicroservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TokenController {

    @PostMapping("/registerToken")
    public ResponseEntity<String> registerToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        System.out.println("✅ Received Expo Token: " + token);
        return ResponseEntity.ok("Token received successfully");
    }
}
