package com.example.project.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import com.example.project.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class AddCustomerActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPhone, edtPassword;
    private Button btnCreateCustomer;
    private ImageView btnBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnCreateCustomer = findViewById(R.id.btnCreateCustomer);
        btnBack = findViewById(R.id.btnBack);

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút tạo khách hàng
        btnCreateCustomer.setOnClickListener(v -> createCustomer());
    }

    private void createCustomer() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Hãy nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = phone + "@gmail.com";

        // Tạo tài khoản Firebase Auth
        mAuth.createUserWithEmailAndPassword(username, password)
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();

                    Map<String, Object> profile = new HashMap<>();
                    profile.put("name", name);
                    profile.put("email", email);
                    profile.put("phone", phone);
                    profile.put("role", "customer");
                    profile.put("createdAt", new Timestamp(new Date()));

                    // Lưu Firestore
                    db.collection("users").document(uid)
                            .set(profile)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Tạo khách hàng thành công!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(this, AddAccountActivity.class);
                                intent.putExtra("userId", uid);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Lỗi lưu Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tạo tài khoản: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
