package com.example.project.Activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPinActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private Button btnVerifyPIN;
    private ImageView btnBack;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupPINInputs();

        btnVerifyPIN.setOnClickListener(v -> savePIN());
        btnBack.setOnClickListener(v -> finish());
    }

    private void initViews() {
        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);

        btnVerifyPIN = findViewById(R.id.btnVerifyPIN);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupPINInputs() {
        EditText[] otps = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < otps.length; i++) {
            final int index = i;
            otps[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otps.length - 1) {
                        // Nếu nhập 1 ký tự, nhảy sang ô tiếp theo
                        otps[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        // Nếu xóa ký tự, lùi về ô trước
                        otps[index - 1].requestFocus();
                        otps[index - 1].setSelection(otps[index - 1].getText().length());
                    }
                }
            });
        }
    }

    /** Lưu mã PIN vào Firestore */
    private void savePIN() {
        String pin =
                otp1.getText().toString().trim() +
                        otp2.getText().toString().trim() +
                        otp3.getText().toString().trim() +
                        otp4.getText().toString().trim() +
                        otp5.getText().toString().trim() +
                        otp6.getText().toString().trim();

        if (pin.length() != 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 số PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        if (userId == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Data lưu Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("pin", pin);

        db.collection("users")
                .document(userId)
                .update(data)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AddPinActivity.this, "Tạo PIN thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // hoặc chuyển sang màn khác
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddPinActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}
