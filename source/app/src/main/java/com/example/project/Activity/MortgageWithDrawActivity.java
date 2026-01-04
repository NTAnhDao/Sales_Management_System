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

public class MortgageWithDrawActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvAccountNumber, tvBalance;
    private TextView tvAccountNumberMortgage, tvBalanceMortgage;
    private EditText edtAmount, edtDescription;
    private Button btnWithDraw;


    private FirebaseFirestore db;
    private String uid;
    private String mortgageAccountId;
    private String paymentAccountId;

    private int paymentBalance = 0;
    private int mortgageBalance = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mortgage_withdraw);

        btnBack = findViewById(R.id.btnBack);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        tvAccountNumberMortgage = findViewById(R.id.tvAccountNumberMortgage);
        tvBalanceMortgage = findViewById(R.id.tvBalanceMortgage);
        edtAmount = findViewById(R.id.edtAmount);
        edtDescription = findViewById(R.id.edtContent);
        btnWithDraw = findViewById(R.id.btnTransaction);
        edtDescription.setText("Rút tiền từ khoản vay");


        btnBack.setOnClickListener(v -> finish());

        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();

        mortgageAccountId = getIntent().getStringExtra("mortgageAccountId");
        if (mortgageAccountId == null) {
            Toast.makeText(this, "Thiếu accountId khoản vay", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadMortgageAccount();
        loadPaymentAccount();
        btnWithDraw.setOnClickListener(v -> handleWithDraw());

        edtAmount.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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

    /**
     * Load tài khoản vay
     */
    private void loadMortgageAccount() {
        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(mortgageAccountId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    if (!"Vay".equals(doc.getString("accountType"))) {
                        Toast.makeText(this, "Không phải tài khoản vay", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }

                    tvAccountNumberMortgage.setText(doc.getString("accountNumber"));
                    mortgageBalance = doc.getLong("balance").intValue();
                    tvBalanceMortgage.setText(String.format("%,d VNĐ", mortgageBalance));
                });
    }

    /**
     * Load tài khoản thanh toán
     */
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
                        return;
                    }

                    DocumentSnapshot doc = snap.getDocuments().get(0);

                    paymentAccountId = doc.getId();
                    tvAccountNumber.setText(doc.getString("accountNumber"));
                    paymentBalance = doc.getLong("balance").intValue();
                    tvBalance.setText(String.format("%,d VNĐ", paymentBalance));
                });
    }

    /**
     * RÚT tiền từ khoản vay
     */
    private void handleWithDraw() {
        String amountStr = edtAmount.getText().toString().replace(".", "").trim();
        String content = edtDescription.getText().toString().replace(".", "").trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr.replace(".", ""));

        if (amount < 10000) {
            Toast.makeText(this, "Tối thiểu 10.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > mortgageBalance) {
            Toast.makeText(this, "Số dư không đủ", Toast.LENGTH_SHORT).show();
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

                    // ====== GIẢ LẬP TRANSFER FIELD (VERIFY EXPECT) ======
                    intent.putExtra("toFullname", "Tài khoản vay");
                    intent.putExtra("toAccountNumber", tvAccountNumberMortgage.getText().toString());
                    intent.putExtra("bankId", "INTERNAL");

                    // ====== RIÊNG CHO MORTGAGE ======
                    intent.putExtra("type", "MORTGAGE_WITHDRAW");
                    intent.putExtra("paymentAccountId", paymentAccountId);
                    intent.putExtra("mortgageAccountId", mortgageAccountId);

                    startActivity(intent);
                });
    }

}
