package com.example.ConsumerMicroservice.model;

import java.io.Serializable;
import java.util.Map;

public class NotificationMessage implements Serializable {

    private String notificationId;
    private String userId;
    private String type;       // "priority" or "non-priority"
    private String title;
    private String body;
    private String channel;    // "email", "sms", "push"
    private Map<String, Object> metadata; // flexible metadata

    // Default constructor (required for Jackson)
    public NotificationMessage() {
    }

    // Parameterized constructor
    public NotificationMessage(String notificationId, String userId, String type, String title,
                               String body, String channel, Map<String, Object> metadata) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.channel = channel;
        this.metadata = metadata;
    }

    // Getters and Setters

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "NotificationMessage{" +
                "notificationId='" + notificationId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", channel='" + channel + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
