package com.example.project.Model;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String avatar;

    public Customer() {}

    public Customer(String id, String name, String phone, String avatar) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAvatar() { return avatar; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAvatar(String avatarUrl) { this.avatar = avatar; }
}
