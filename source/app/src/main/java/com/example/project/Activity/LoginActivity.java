package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        String email = username + "@gmail.com";

        if(username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Lấy UID để truy xuất role
                    String uid = mAuth.getCurrentUser().getUid();
                    getUserRole(uid);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_LONG).show()
                );
    }

    private void getUserRole(String uid) {
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if(document.exists()) {
                        String role = document.getString("role");

                        if(role == null) {
                            Toast.makeText(this, "User chưa có role!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Điều hướng giao diện
                        redirectByRole(role);
                    } else {
                        Toast.makeText(this, "Không tìm thấy dữ liệu User!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void redirectByRole(String role) {
        if(role.equals("officer")) {
            startActivity(new Intent(this, OfficerMainActivity.class));
        } else {
            startActivity(new Intent(this, UserMainActivity.class));
        }
        finish();
    }
}

