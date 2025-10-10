package com.example.ConsumerMicroservice.service;

import com.example.ConsumerMicroservice.model.NotificationMessage;
import com.example.ConsumerMicroservice.model.FailedMessage;
import com.example.ConsumerMicroservice.repository.FailedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final RetryHandler retryHandler;
    private final FailedMessageRepository failedMessageRepository;

    private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

    public void deliver(NotificationMessage message) {
        try {
            log.info("🚀 Delivering message {} (type: {}) to user {}",
                    message.getNotificationId(), message.getType(), message.getUserId());

            // Try sending via Expo Push
            retryHandler.retry(message, () -> sendExpoPush(message));

            log.info("✅ Successfully delivered message {}", message.getNotificationId());

        } catch (RetryHandler.MaxRetryExceededException e) {
            log.error("❌ Max retries exceeded for {}. Saving to DB. Reason: {}",
                    message.getNotificationId(), e.getMessage());
            saveFailedMessage(message, e.getMessage(), e.getRetryCount());
        } catch (Exception e) {
            log.error("💥 Unexpected delivery error for {}", message.getNotificationId(), e);
            saveFailedMessage(message, e.getMessage(), 0);
        }
    }

    @Async("asyncExecutor")
    private void sendExpoPush(NotificationMessage message) throws Exception {
        // Extract token
        String expoPushToken = null;
        if (message.getMetadata() != null) {
            Object tokenObj = message.getMetadata().get("expoPushToken");
            if (tokenObj != null) {
                expoPushToken = String.valueOf(tokenObj);
            }
        }

        // Handle missing token
        if (expoPushToken == null || expoPushToken.isEmpty()) {
            log.warn("⚠️ Skipping Expo push for {} — missing expoPushToken", message.getNotificationId());
            throw new Exception("Missing Expo Push Token for user " + message.getUserId());
        }

        log.info("📲 Sending Expo push to token: {}", expoPushToken);

        //  Build payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("to", expoPushToken);
        payload.put("sound", "default");
        payload.put("title", message.getTitle());
        payload.put("body", message.getBody());
        payload.put("data", message.getMetadata());

        //  Send request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(EXPO_PUSH_URL, request, String.class);

        // Validate response
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("❌ Expo Push failed with status {} and body {}",
                    response.getStatusCode(), response.getBody());
            throw new Exception("Expo Push API failed for token " + expoPushToken);
        }

        log.info(" Expo Push sent successfully: {}", response.getBody());
    }

    private void saveFailedMessage(NotificationMessage msg, String reason, int retryCount) {
        Map<String, String> metadataStr = null;
        if (msg.getMetadata() != null) {
            metadataStr = msg.getMetadata().entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));
        }

        FailedMessage failed = new FailedMessage(
                msg.getNotificationId(),
                msg.getUserId(),
                msg.getType(),
                msg.getTitle(),
                msg.getBody(),
                msg.getChannel(),
                metadataStr,
                Instant.now(),
                retryCount,
                reason
        );

        failedMessageRepository.save(failed);
        log.info("💾 Saved failed message {} to DB", msg.getNotificationId());
    }
}
