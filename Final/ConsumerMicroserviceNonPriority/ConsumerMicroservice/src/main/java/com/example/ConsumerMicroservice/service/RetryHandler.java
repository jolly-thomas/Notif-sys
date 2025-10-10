package com.example.ConsumerMicroservice.service;

import com.example.ConsumerMicroservice.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RetryHandler {

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000; // 2s delay

    @FunctionalInterface
    public interface RetryableOperation {
        void execute() throws Exception;
    }

    public void retry(NotificationMessage message, RetryableOperation operation) throws MaxRetryExceededException {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                operation.execute();
                return; // success
            } catch (Exception e) {
                attempt++;
                log.warn("⚠️ Retry {}/{} failed for message {}: {}", attempt, MAX_RETRIES,
                        message.getNotificationId(), e.getMessage());

                if (attempt >= MAX_RETRIES) {
                    throw new MaxRetryExceededException("Retries exhausted", attempt);
                }

                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new MaxRetryExceededException("Retry interrupted", attempt);
                }
            }
        }
    }

    public static class MaxRetryExceededException extends Exception {
        private final int retryCount;

        public MaxRetryExceededException(String message, int retryCount) {
            super(message);
            this.retryCount = retryCount;
        }

        public int getRetryCount() {
            return retryCount;
        }
    }
}
