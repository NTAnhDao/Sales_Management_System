package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerifyPINActivity extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView txtTimerPin;
    private Button btnVerifyPIN;

    private CountDownTimer pinTimer;
    private long timeLeft = 100; // 100 giây

    private String correctPIN = null; // Lấy từ Firestore

    private String email, name, accountNumber, toFullname, toAccountNumber, description, bankId, balance;
    private int amount;
    private Intent i;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String type;
    private String billId;
    private String providerCode;
    private String customerCode;
    private String paymentAccountId;
    private String mortgageAccountId;
    private String savingAccountId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_pin);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        txtTimerPin = findViewById(R.id.txtTimerPin);
        btnVerifyPIN = findViewById(R.id.btnVerifyPIN);

        btnVerifyPIN.setEnabled(false); // Disable trước khi load PIN xong

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getDataFromIntent();
        setupInputs();
        startPinTimer();
        loadAccountPIN();

        btnVerifyPIN.setOnClickListener(v -> checkPIN());
    }

    private void getDataFromIntent() {
        i = getIntent();
        email = i.getStringExtra("email");
        name = i.getStringExtra("name");
        accountNumber = i.getStringExtra("accountNumber");
        toFullname = i.getStringExtra("toFullname");
        toAccountNumber = i.getStringExtra("toAccountNumber");
        description = i.getStringExtra("description");
        bankId = i.getStringExtra("bankId");
        amount = i.getIntExtra("amount", 0);
        balance = i.getStringExtra("balance");
        type = i.getStringExtra("type");
        billId = i.getStringExtra("billId");
        providerCode = i.getStringExtra("providerCode");
        customerCode = i.getStringExtra("customerCode");
        paymentAccountId = i.getStringExtra("paymentAccountId");
        mortgageAccountId = i.getStringExtra("mortgageAccountId");
        savingAccountId = i.getStringExtra("savingAccountId");

    }

    private void setupInputs() {
        EditText[] arr = {otp1, otp2, otp3, otp4, otp5, otp6};

        for (int i = 0; i < arr.length; i++) {
            int index = i;
            arr[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < arr.length - 1) {
                        // Nhảy sang ô tiếp theo
                        arr[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        // Lùi về ô trước nếu xóa
                        arr[index - 1].requestFocus();
                        arr[index - 1].setSelection(arr[index - 1].getText().length());
                    }
                }
            });
        }
    }

    private void startPinTimer() {
        pinTimer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                String formatted = String.format("%02d:%02d", sec / 60, sec % 60);
                txtTimerPin.setText("⏳ " + formatted);
            }

            @Override
            public void onFinish() {
                txtTimerPin.setText("⏳ 00:00");
                Toast.makeText(VerifyPINActivity.this, "Hết thời gian nhập PIN!", Toast.LENGTH_LONG).show();
                finish();
            }
        }.start();
    }

    private void loadAccountPIN() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        correctPIN = doc.getString("pin"); // Lấy trực tiếp PIN từ users

                        if (correctPIN == null) {
                            Toast.makeText(this, "Bạn chưa tạo mã PIN!", Toast.LENGTH_SHORT).show();
                        } else {
                            btnVerifyPIN.setEnabled(true);
                        }
                    } else {
                        Toast.makeText(this, "Không tìm thấy người dùng!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi tải PIN: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }

    private void checkPIN() {
        if (correctPIN == null) {
            Toast.makeText(this, "Đang tải PIN, vui lòng đợi...", Toast.LENGTH_SHORT).show();
            return;
        }

        String pin = otp1.getText().toString() +
                otp2.getText().toString() +
                otp3.getText().toString() +
                otp4.getText().toString() +
                otp5.getText().toString() +
                otp6.getText().toString();

        if (pin.length() != 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 số PIN!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pin.equals(correctPIN)) {
            Toast.makeText(this, "PIN không chính xác!", Toast.LENGTH_SHORT).show();
            return;
        }

        pinTimer.cancel();

        // ck sô tiền lớn hơn thì xác thực gương mặt
        if(amount >= 10000000){
            i = new Intent(VerifyPINActivity.this, FaceAuthActivity.class);
        } else {
            // Chuyển sang VerifyOTPActivity
            i = new Intent(VerifyPINActivity.this, VerifyOTPActivity.class);
        }

        i.putExtra("email", email);
        i.putExtra("name", name);
        i.putExtra("accountNumber", accountNumber);
        i.putExtra("toFullname", toFullname);
        i.putExtra("toAccountNumber", toAccountNumber);
        i.putExtra("description", description);
        i.putExtra("bankId", bankId);
        i.putExtra("amount", amount);
        i.putExtra("balance", balance);

        i.putExtra("type", type);
        i.putExtra("billId", billId);
        i.putExtra("providerCode", providerCode);
        i.putExtra("customerCode", customerCode);
        i.putExtra("paymentAccountId", paymentAccountId);
        i.putExtra("mortgageAccountId", mortgageAccountId);
        i.putExtra("savingAccountId", savingAccountId);


        startActivity(i);
        finish();
    }

}
