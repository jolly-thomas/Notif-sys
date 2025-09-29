package com.example.producer_not.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class NotificationMessage {

    // Setters
    // Getters
    private String id;
    private String title;
    private String body;
    private String type; // PRIORITY / NONPRIORITY
    private Instant timestamp = Instant.now();

    public NotificationMessage() {
    }

    public NotificationMessage(String id, String title, String body, String type, Instant timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.type = type;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
