package com.example.project.Activity;

import com.google.firebase.auth.FirebaseAuth;

public class UserSession {
    public static String getUid() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return null;
    }

    public static void logout() {
        FirebaseAuth.getInstance().signOut();
    }
}
