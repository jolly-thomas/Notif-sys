package com.example.consumer_not.model;

import java.time.Instant;

public class NotificationMessage {
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

    // Getters
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTimestamp(Instant timestamp) {
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
