package com.example.ConsumerMicroservice.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "failed_messages")
public class FailedMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String notificationId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String type;  // "priority" or "non-priority"

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String body;

    @Column(nullable = false)
    private String channel;  // "email", "sms", "push"

    @ElementCollection
    @CollectionTable(
            name = "failed_message_metadata",
            joinColumns = @JoinColumn(name = "failed_message_id")
    )
    @MapKeyColumn(name = "meta_key")
    @Column(name = "meta_value")
    private Map<String, String> metadata; // Always String

    @Column(nullable = false)
    private Instant failedAt; // timestamp when failure occurred

    @Column(nullable = false)
    private int retryCount;   // number of retry attempts

    @Column(nullable = true, length = 1000)
    private String failureReason; // why it failed

    public FailedMessage() {}

    public FailedMessage(String notificationId, String userId, String type, String title,
                         String body, String channel, Map<String, String> metadata,
                         Instant failedAt, int retryCount, String failureReason) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.channel = channel;
        this.metadata = metadata;
        this.failedAt = failedAt;
        this.retryCount = retryCount;
        this.failureReason = failureReason;
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNotificationId() { return notificationId; }
    public void setNotificationId(String notificationId) { this.notificationId = notificationId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    public Instant getFailedAt() { return failedAt; }
    public void setFailedAt(Instant failedAt) { this.failedAt = failedAt; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    @Override
    public String toString() {
        return "FailedMessage{" +
                "notificationId='" + notificationId + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", channel='" + channel + '\'' +
                ", metadata=" + metadata +
                ", failedAt=" + failedAt +
                ", retryCount=" + retryCount +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
