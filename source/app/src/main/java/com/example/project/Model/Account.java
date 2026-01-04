package com.example.project.Model;

public class Account {
    private String accountId;
    private String accountNumber;
    private double balance;
    private String type;

    public Account(String accountId, String accountNumber, double balance, String type) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.type = type;
    }

    public String getId() { return accountId; }
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getType() { return type; }
    public void setId(String accountId) { this.accountId = accountId; }

}
