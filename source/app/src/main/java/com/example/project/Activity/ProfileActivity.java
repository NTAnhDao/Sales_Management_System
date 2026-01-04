package com.example.project.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.*;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project.R;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private TextView tvAccName, tvName, tvPhone, tvEmail;
    private ImageView imgAvatar, icEdit, btnBack;
    private LinearLayout containerBiometric, containerChangePass, containerLogout, containerAddPin;
    private FirebaseFirestore db;
    private String userId;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        tvAccName = findViewById(R.id.tvAccName);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);

        imgAvatar = findViewById(R.id.imgAvatar);
        icEdit = findViewById(R.id.icEdit);
        btnBack = findViewById(R.id.btnBack);

        containerBiometric = findViewById(R.id.container_biometric);
        containerChangePass = findViewById(R.id.container_changePass);
        containerLogout = findViewById(R.id.container_logout);
        containerAddPin = findViewById(R.id.container_addPIN);

        // Firestore
        db = FirebaseFirestore.getInstance();

        intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        }

        if (userId == null || userId.isEmpty()) {
            userId = UserSession.getUid();
        }

        loadUserData();
        handleClicks();
    }

    private void loadUserData() {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {
                        bindUserData(document);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show()
                );
    }

    private void bindUserData(DocumentSnapshot doc) {
        String name = doc.getString("name");
        String phone = doc.getString("phone");
        String email = doc.getString("email");
        String role = doc.getString("role");

        if (role.equals("customer")) {
            String avatarBase64 = doc.getString("avatar");

            // Load avatar nếu có
            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                byte[] imageBytes = Base64.decode(avatarBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imgAvatar.setImageBitmap(bitmap);
            } else {
                imgAvatar.setImageResource(R.drawable.img_avatar);
            }
        }

        // Gán dữ liệu
        tvAccName.setText(name);
        tvName.setText(name);
        tvPhone.setText(phone);
        tvEmail.setText(email);

    }

    private void handleClicks() {

        btnBack.setOnClickListener(v -> {
            finish();
        });

        icEdit.setOnClickListener(v -> {
            intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userId", userId);
            launcher.launch(intent);
        });

        containerAddPin.setOnClickListener(v -> {
            startActivity(new Intent(this, AddPinActivity.class));
        });

        containerChangePass.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });

        containerLogout.setOnClickListener(v -> {
            UserSession.logout();

            // Chuyển về màn hình Login
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        containerBiometric.setOnClickListener(v -> {
            intent = new Intent(this, eKYCActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Activity edit xong reload dữ liệu
                    loadUserData();
                }
            });

}

