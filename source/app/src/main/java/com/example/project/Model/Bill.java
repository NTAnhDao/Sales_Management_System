package com.example.project.Model;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Bill {
    private String id;
    private String providerCode;
    private String customerCode;
    private long amount;
    private String period;
    private String status;
    private String type;
    private Date createdAt;

    public Bill() {
    }

    public Bill(String id, String providerCode, String customerCode, long amount, String period, String status, String type, Date createdAt) {
        this.id = id;
        this.providerCode = providerCode;
        this.customerCode = customerCode;
        this.amount = amount;
        this.period = period;
        this.status = status;
        this.type = type;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

}
