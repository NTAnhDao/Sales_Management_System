package com.example.project.Model;

    public class Bank {
        private String name;
        private String code;
        private String logoUrl;

        public Bank() {}  // cần cho Firestore

        public Bank(String name, String code, String logoUrl) {
            this.name = name;
            this.code = code;
            this.logoUrl = logoUrl;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getLogoUrl() { return logoUrl; }
        public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

        @Override
        public String toString() {
            return code;
        }

    }


