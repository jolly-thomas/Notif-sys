package com.example.ConsumerMicroservice.service;

import com.example.ConsumerMicroservice.model.NotificationMessage;
import com.example.ConsumerMicroservice.model.FailedMessage;
import com.example.ConsumerMicroservice.repository.FailedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final RetryHandler retryHandler;
    private final FailedMessageRepository failedMessageRepository;

    public void deliver(NotificationMessage message) {
        try {

            log.info("🚀 Delivering message {} to user {}", message.getNotificationId(), message.getUserId());

            retryHandler.retry(message, () -> sendFcm(message));

            log.info("✅ Successfully delivered message {}", message.getNotificationId());

        } catch (RetryHandler.MaxRetryExceededException e) {
            log.error("❌ Max retries exceeded for {}. Saving to DB. Reason: {}",
                    message.getNotificationId(), e.getMessage());
            saveFailedMessage(message, e.getMessage(), e.getRetryCount());
        }
    }
    @Async("asyncExecutor")
    private void sendFcm(NotificationMessage message) throws Exception {
        boolean appInForeground = checkAppForegroundStatus(message.getUserId());
        boolean sent = pushToFcm(message);

        if (!sent) {
            throw new Exception("FCM delivery failed for " + message.getNotificationId() +
                    " (App in foreground: " + appInForeground + ")");
        }
    }

    private boolean checkAppForegroundStatus(String userId) {
        // TODO integrate with FCM/your client heartbeat
        return false; // assume background for now
    }

    private boolean pushToFcm(NotificationMessage message) {
        // TODO integrate with Firebase Admin SDK
        log.info("📲 Pushing message {} to FCM for user {}", message.getNotificationId(), message.getUserId());
        return true; // simulate success
    }

    private void saveFailedMessage(NotificationMessage msg, String reason, int retryCount) {
        // Convert metadata to Map<String, String>
        Map<String, String> metadataStr = null;
        if (msg.getMetadata() != null) {
            metadataStr = msg.getMetadata().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> String.valueOf(e.getValue())
                    ));
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
