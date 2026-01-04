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

public class DepositPhoneActivity extends AppCompatActivity {

    private TextView tvAccountNumber, tvBalance;
    private EditText edtPhone, edtAmount, edtContent;
    private Button btnTransaction;
    private ImageView btnBack;

    private FirebaseFirestore db;
    private String uid;

    private String paymentAccountId;
    private long paymentBalance;

    /* ===== LIMIT ===== */
    private static final int MAX_AMOUNT_PER_TRANSACTION = 5_000_000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_phone);

        // ====== INIT VIEW ======
        btnBack = findViewById(R.id.btnBack);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        edtPhone = findViewById(R.id.edtPhone);
        edtAmount = findViewById(R.id.edtAmount);
        edtContent = findViewById(R.id.edtContent);
        btnTransaction = findViewById(R.id.btnTransaction);

        edtContent.setText("Nạp tiền điện thoại");

        btnBack.setOnClickListener(v -> finish());

        // ====== FIREBASE ======
        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();

        // ====== FORMAT AMOUNT ======
        edtAmount.addTextChangedListener(new TextWatcher() {
            boolean isEditing = false;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;

                String raw = s.toString().replace(".", "").trim();
                if (raw.isEmpty()) {
                    isEditing = false;
                    return;
                }

                try {
                    long value = Long.parseLong(raw);

                    if (value > MAX_AMOUNT_PER_TRANSACTION) {
                        Toast.makeText(
                                DepositPhoneActivity.this,
                                "Mỗi lần nạp tối đa 5.000.000đ",
                                Toast.LENGTH_SHORT
                        ).show();
                        value = MAX_AMOUNT_PER_TRANSACTION;
                    }

                    DecimalFormat df = new DecimalFormat("#,###");
                    String formatted = df.format(value).replace(",", ".");
                    edtAmount.setText(formatted);
                    edtAmount.setSelection(formatted.length());
                } catch (Exception ignored) {}

                isEditing = false;
            }
        });

        loadPaymentAccount();

        btnTransaction.setOnClickListener(v -> handleDeposit());
    }

    /* ================= LOAD PAYMENT ACCOUNT ================= */
    private void loadPaymentAccount() {
        db.collection("users").document(uid)
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

    /* ================= HANDLE DEPOSIT PHONE ================= */
    private void handleDeposit() {
        String phone = edtPhone.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().replace(".", "").trim();
        String content = edtContent.getText().toString().trim();

        // ===== VALIDATE PHONE =====
        if (!phone.matches("^0\\d{9}$")) {
            Toast.makeText(this, "Số điện thoại phải đủ 10 số và bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr);

        if (amount < 10_000) {
            Toast.makeText(this, "Tối thiểu 10.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > MAX_AMOUNT_PER_TRANSACTION) {
            Toast.makeText(this, "Mỗi lần nạp tối đa 5.000.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (paymentBalance < amount) {
            Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // ====== GET USER INFO ======
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {
                    if (!userDoc.exists()) return;

                    String email = userDoc.getString("email");
                    String name = userDoc.getString("name");

                    Intent intent = new Intent(this, VerifyPINActivity.class);

                    // ====== COMMON ======
                    intent.putExtra("amount", amount);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("accountNumber", tvAccountNumber.getText().toString());
                    intent.putExtra("description", content);

                    // ====== PHONE DEPOSIT INFO ======
                    intent.putExtra("toFullname", "Nạp tiền điện thoại");
                    intent.putExtra("toAccountNumber", phone);
                    intent.putExtra("bankId", "MOBILE");

                    // ====== TYPE ======
                    intent.putExtra("type", "PHONE_DEPOSIT");
                    intent.putExtra("paymentAccountId", paymentAccountId);

                    startActivity(intent);
                });
    }
}
