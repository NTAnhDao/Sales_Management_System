package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class SavingWithDrawActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvAccountNumber, tvBalance;
    private TextView tvAccountNumberSaving, tvBalanceSaving;
    private EditText edtAmount, edtDescription;
    private Button btnWithDraw;

    private FirebaseFirestore db;
    private String uid;

    private String savingAccountId;
    private String paymentAccountId;

    private long paymentBalance = 0;
    private long savingBalance = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving_withdraw);

        btnBack = findViewById(R.id.btnBack);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        tvAccountNumberSaving = findViewById(R.id.tvAccountNumberSaving);
        tvBalanceSaving = findViewById(R.id.tvBalanceSaving);
        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtContent);
        btnWithDraw = findViewById(R.id.btnTransaction);

        edtDescription.setText("Rút tiền từ tài khoản tiết kiệm");
        btnBack.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();

        savingAccountId = getIntent().getStringExtra("savingAccountId");
        if (savingAccountId == null) {
            Toast.makeText(this, "Thiếu accountId tiết kiệm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadSavingAccount();
        loadPaymentAccount();
        btnWithDraw.setOnClickListener(v -> handleWithDraw());

        // ===== FORMAT TIỀN =====
        edtAmount.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String raw = s.toString().replace(".", "").trim();
                if (raw.isEmpty()) {
                    edtAmount.setText("");
                    isEditing = false;
                    return;
                }

                try {
                    long value = Long.parseLong(raw);
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    String formatted = formatter.format(value).replace(",", ".");
                    edtAmount.setText(formatted);
                    edtAmount.setSelection(formatted.length());
                } catch (Exception ignored) {}

                isEditing = false;
            }
        });
    }

    /* ---------------- LOAD TÀI KHOẢN TIẾT KIỆM ---------------- */
    private void loadSavingAccount() {
        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(savingAccountId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    if (!"Tiết kiệm".equals(doc.getString("accountType"))) {
                        Toast.makeText(this, "Không phải tài khoản tiết kiệm", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    tvAccountNumberSaving.setText(doc.getString("accountNumber"));
                    savingBalance = doc.getLong("balance");
                    tvBalanceSaving.setText(String.format("%,.0f VNĐ", (double) savingBalance));
                });
    }

    /* ---------------- LOAD TÀI KHOẢN THANH TOÁN ---------------- */
    private void loadPaymentAccount() {
        db.collection("users")
                .document(uid)
                .collection("accounts")
                .whereEqualTo("accountType", "Thanh toán")
                .limit(1)
                .get()
                .addOnSuccessListener(snap -> {
                    if (snap.isEmpty()) {
                        Toast.makeText(this, "Không có tài khoản thanh toán", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    DocumentSnapshot doc = snap.getDocuments().get(0);

                    paymentAccountId = doc.getId();
                    paymentBalance = doc.getLong("balance");

                    tvAccountNumber.setText(doc.getString("accountNumber"));
                    tvBalance.setText(String.format("%,.0f VNĐ", (double) paymentBalance));
                });
    }

    /* ---------------- RÚT TIỀN ---------------- */
    private void handleWithDraw() {
        String amountStr = edtAmount.getText().toString().replace(".", "").trim();
        String content = edtDescription.getText().toString().trim();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr.replace(".", ""));

        if (amount < 10000) {
            Toast.makeText(this, "Tối thiểu 10.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > savingBalance) {
            Toast.makeText(this, "Số dư tiết kiệm không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // 👉 LẤY EMAIL USER
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {

                    if (!userDoc.exists()) {
                        Toast.makeText(this, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String email = userDoc.getString("email");
                    String name = userDoc.getString("name");

                    if (email == null || email.isEmpty()) {
                        Toast.makeText(this, "Không có email người dùng", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(this, VerifyPINActivity.class);

                    // ====== CHUNG ======
                    intent.putExtra("amount", amount);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("accountNumber", tvAccountNumber.getText().toString());
                    intent.putExtra("description", content);

                    // ====== GIẢ LẬP TRANSFER ======
                    intent.putExtra("toFullname", "Tài khoản tiết kiệm");
                    intent.putExtra("toAccountNumber", tvAccountNumberSaving.getText().toString());
                    intent.putExtra("bankId", "INTERNAL");

                    // ====== RIÊNG CHO SAVING ======
                    intent.putExtra("type", "SAVING_WITHDRAW");
                    intent.putExtra("paymentAccountId", paymentAccountId);
                    intent.putExtra("savingAccountId", savingAccountId);

                    startActivity(intent);
                });
    }
}
