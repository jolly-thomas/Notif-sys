package com.example.consumer_not.service;

import com.example.consumer_not.model.NotificationMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    @JmsListener(destination = "priority.queue")
    public void consumePriority(NotificationMessage msg) {
        System.out.println(">>> PRIORITY: " + msg.getTitle() + " - " + msg.getBody());
    }

    @JmsListener(destination = "nonpriority.queue")
    public void consumeNonPriority(NotificationMessage msg) {
        System.out.println(">>> NONPRIORITY: " + msg.getTitle() + " - " + msg.getBody());
    }
}
