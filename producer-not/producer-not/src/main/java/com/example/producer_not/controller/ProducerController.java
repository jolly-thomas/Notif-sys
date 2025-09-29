package com.example.producer_not.controller;

import com.example.producer_not.model.NotificationMessage;
import com.example.producer_not.service.NotificationProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notify")
public class ProducerController {

    private final NotificationProducer producer;

    public ProducerController(NotificationProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> send(@RequestBody NotificationMessage msg) {
        producer.send(msg);
        return ResponseEntity.ok("Message queued!");
    }
}
