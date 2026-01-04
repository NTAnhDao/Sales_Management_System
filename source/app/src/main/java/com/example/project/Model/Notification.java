package com.example.project.Model;

import java.util.Date;

public class Notification {
    private String id;
    private String accountId;
    private String message;
    private String type;       // "SEND" / "RECEIVE"
    private long amount;       // số tiền + hoặc -
    private long timestamp;

    public Notification() {}

    public Notification(String id, String accountId, String message, String type, long amount, long timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.message = message;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAccountId() { return accountId; }
    public void setUserId(String accountId) { this.accountId = accountId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
