package com.example.project.Model;


import java.util.Date;

public class Transaction {

    private String id;
    private String accountId;
    private double amount;
    private String bankId;
    private String description;
    private String status;        // paid / pending / failed
    private String toAccountNumber;
    private String toFullname;
    private String type;
    private Date timestamp;

    public Transaction() { }

    public Transaction(String id, String accountId, double amount, String bankId,
                       String description, String status,
                       String toAccountNumber, String toFullname, String type,
                       Date timestamp) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.bankId = bankId;
        this.description = description;
        this.status = status;
        this.toAccountNumber = toAccountNumber;
        this.toFullname = toFullname;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Getter / Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public double getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getBankId() { return bankId; }
    public void setBankId(String bankId) { this.bankId = bankId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getToAccountNumber() { return toAccountNumber; }
    public void setToAccountNumber(String toAccountNumber) { this.toAccountNumber = toAccountNumber; }

    public String getToFullname() { return toFullname; }
    public void setToFullname(String toFullname) { this.toFullname = toFullname; }

    public String getType(){ return type; }
    public void setType(String type){ this.type = type; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }


}
