package com.example.project.Model;



import java.io.Serializable;
public class Electricity{


    private String id;          // documentId Firestore
    private String code;        // EVNSPC
    private String name;        // TCT Điện Lực Miền Nam EVNSPC
    private String logoUrl;     // logo
    private String type;        // ELECTRICITY

    // 🔹 BẮT BUỘC: constructor rỗng cho Firestore
    public Electricity() {}

    public Electricity(String id, String code, String name, String logoUrl, String type) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.logoUrl = logoUrl;
        this.type = type;
    }

    // ===== GETTER / SETTER =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return code;
    }
}
