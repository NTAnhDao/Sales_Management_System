package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddAccountActivity extends AppCompatActivity {
    private EditText edtAccountNumber, edtBalance;
    private Spinner spnAccountType;
    private RadioGroup rgPaymentMode;
    private RadioButton rbMonthly, rbBiweekly;
    private ImageView btnBack;
    private Button btnAddAccount;
    private String[] accountTypes = {"Thanh toán", "Tiết kiệm", "Vay"};

    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acc);

        edtAccountNumber = findViewById(R.id.edtAccountNumber);
        edtBalance = findViewById(R.id.edtBalance);
        rgPaymentMode = findViewById(R.id.rgPaymentMode);
        rbMonthly = findViewById(R.id.rbMonthly);
        rbBiweekly = findViewById(R.id.rbBiweekly);

        btnAddAccount = findViewById(R.id.btnAddAccount);
        btnBack = findViewById(R.id.btnBack);

        // Spinner tài khoản
        spnAccountType = findViewById(R.id.spnAccountType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, accountTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAccountType.setAdapter(adapter);
        spnAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = accountTypes[position];

                if (type.equals("Vay")) {
                    rgPaymentMode.setVisibility(View.VISIBLE);   // Hiện
                } else {
                    rgPaymentMode.setVisibility(View.GONE);      // Ẩn
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
        }

        // Nút thêm tài khoản
        btnAddAccount.setOnClickListener(v -> addAccount());

        btnBack.setOnClickListener(v -> finish());
    }

    private void addAccount() {
        String bankCode = "DNSBank (DNS)";
        String accNumber = edtAccountNumber.getText().toString();
        String balance = edtBalance.getText().toString();
        String type = spnAccountType.getSelectedItem().toString();

        if (accNumber.isEmpty() || balance.isEmpty()) {
            Toast.makeText(this, "Nhập đủ thông tin tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!accNumber.matches("\\d{10}")) {
            Toast.makeText(this, "Số tài khoản phải có đúng 10 chữ số!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> account = new HashMap<>();
        account.put("accountNumber", accNumber);
        account.put("balance", Integer.parseInt(balance));
        account.put("bankId", bankCode);
        account.put("accountType", type);
        account.put("createdAt", new Timestamp(new Date()));

        if (type.equals("Vay")) {
            account.put("amountLoan", Integer.parseInt(balance));
            account.put("loanTermYears", 2);

            // Lấy payment mode từ radio group
            int checkedId = rgPaymentMode.getCheckedRadioButtonId();
            if (checkedId == -1) {
                Toast.makeText(this, "Vui lòng chọn chế độ trả khoản vay!", Toast.LENGTH_SHORT).show();
                return;
            }
            String paymentMode = "monthly";

            if (checkedId == R.id.rbBiweekly) {
                paymentMode = "biweekly";
            }

            account.put("paymentMode", paymentMode);
        } else if (type.equals("Tiết kiệm")) {
            account.put("amountProfit", 0);
            account.put("lastProfitMonth", new Date().getMonth() + 1);
            account.put("lastProfitYear", new Date().getYear() + 1900);
        }

        DocumentReference accRef = db.collection("users")
                .document(userId)
                .collection("accounts")
                .document(accNumber);

        accRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                Toast.makeText(this, "Số tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }

            accRef.set(account)
                    .addOnSuccessListener(a -> {
                        Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        edtAccountNumber.setText("");
                        edtBalance.setText("");
                        rgPaymentMode.clearCheck();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        });
    }

}
