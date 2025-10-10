package com.example.ConsumerMicroservice.service;

import com.example.ConsumerMicroservice.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

    private final DeliveryService deliveryService;

    @JmsListener(destination = "${activemq.queue.non-priority}")
    public void consumeNonPriority(NotificationMessage notification) {
        try {
            log.info("📢 Consumed NON-PRIORITY: {}", notification);
            deliveryService.deliver(notification);
        } catch (Exception e) {
            log.error("❌ Error consuming NON-PRIORITY message {}", notification.getNotificationId(), e);
            throw e; // rethrow so ActiveMQ retries
        }
    }

//    @JmsListener(destination = "${activemq.queue.priority}")
//    public void consumePriority(NotificationMessage notification) {
//        try {
//            log.info("📢 Consumed PRIORITY: {}", notification);
//            deliveryService.deliver(notification);
//        } catch (Exception e) {
//            log.error("❌ Error consuming PRIORITY message {}", notification.getNotificationId(), e);
//            throw e; // rethrow so ActiveMQ retries
//        }
//    }
}
