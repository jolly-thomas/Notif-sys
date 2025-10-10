package com.example.ProducerMicroservice.controller;
import lombok.extern.slf4j.Slf4j;
import com.example.ProducerMicroservice.model.NotificationMessage;
import com.example.ProducerMicroservice.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService producerService;

    public NotificationController(NotificationService producerService) {
        this.producerService = producerService;
    }

    /**
     * Endpoint to send a notification
     * @param notification NotificationMessage payload
     * @return ResponseEntity with status
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationMessage notification) {
        try {
            producerService.sendNotification(notification);
            log.info("Success {}", notification.getNotificationId());
            return ResponseEntity.ok("Notification sent successfully to queue: " + notification.getType());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to send notification: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send notification: " + e.getMessage());
        }
    }
}
