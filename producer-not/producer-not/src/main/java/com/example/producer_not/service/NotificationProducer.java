package com.example.producer_not.service;

import com.example.producer_not.model.NotificationMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final JmsTemplate jmsTemplate;
    private static final String PRIORITY_QUEUE = "priority.queue";
    private static final String NONPRIORITY_QUEUE = "nonpriority.queue";

    public NotificationProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(NotificationMessage msg) {
        String queue = "PRIORITY".equalsIgnoreCase(msg.getType()) ? PRIORITY_QUEUE : NONPRIORITY_QUEUE;
        jmsTemplate.convertAndSend(queue, msg);
        System.out.println("Sent message to " + queue + ": " + msg.getTitle());
    }
}
