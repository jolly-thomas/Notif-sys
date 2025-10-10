package com.example.ProducerMicroservice.service;

import com.example.ProducerMicroservice.config.ActiveMQConfig;
import com.example.ProducerMicroservice.model.NotificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    private final JmsTemplate jmsTemplate;
    private final ActiveMQConfig activeMQConfig;




    public NotificationService(JmsTemplate jmsTemplate, ActiveMQConfig activeMQConfig) {
        this.jmsTemplate = jmsTemplate;
        this.activeMQConfig = activeMQConfig;
    }

    /**
     * Send a notification to the appropriate queue based on its type
     * @param notification NotificationMessage object to be sent
     */
    public void sendNotification(NotificationMessage notification) {
        String queueName;

        // Decide the queue based on type
        if ("priority".equalsIgnoreCase(notification.getType())) {
            queueName = activeMQConfig.priorityQueue;
        } else {
            queueName = activeMQConfig.nonPriorityQueue;
        }

        // Send the message using JmsTemplate
        jmsTemplate.convertAndSend(queueName, notification,m -> {
            m.setStringProperty("_type", "NotificationMessage");
            return m;
        });

        // Log for confirmation
        log.info("Sent notification to queue: {}", queueName);
        log.info("Notification: {}", notification.getTitle());
    }
}
