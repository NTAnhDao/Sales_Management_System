package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.*;

import androidx.annotation.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.TransactionAdapter;
import com.example.project.Model.Transaction;
import com.example.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SavingAccountActivity extends AppCompatActivity {
    private TextView tvAccountNumber, tvBalance, tvProfit, tvProfitableRate;
    private ImageView btnBack;
    private EditText etSearch;
    private TransactionAdapter adapter;
    private String accountId, uid;
    private FirebaseFirestore db;
    private List<Transaction> list;
    private double currentBalance;
    private double profitRate;
    private boolean loadedBalance = false, loadedRate = false;
    private LinearLayout containerTopUp, containerWithdraw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saving_account);

        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        tvProfit = findViewById(R.id.tvProfit);
        tvProfitableRate = findViewById(R.id.tvProfitableRate);
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        containerTopUp = findViewById(R.id.containerTopUp);
        containerWithdraw = findViewById(R.id.containerWithdraw);


        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();
        accountId = getIntent().getStringExtra("accountId");

        RecyclerView recycler = findViewById(R.id.recyclerTransactions);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TransactionAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);

        loadAccountInfo();
        loadTransactionHistory();

        btnBack.setOnClickListener(v -> finish());
        containerTopUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingTopUpActivity.class);
            intent.putExtra("savingAccountId", accountId);
            startActivity(intent);

                });

        containerWithdraw.setOnClickListener(v -> {
            Intent intent = new Intent(this, SavingWithDrawActivity.class);
            intent.putExtra("savingAccountId", accountId);
            startActivity(intent);
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAccountInfo() {
        // Lấy tài khoản thanh toán từ subcollection accounts
        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String number = doc.getString("accountNumber");
                        currentBalance = doc.getDouble("balance");

                        tvAccountNumber.setText(number);
                        tvBalance.setText(String.format("%,.0f", currentBalance));
                        loadedBalance = true;
                        triggerProfitCalc();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải tài khoản!", Toast.LENGTH_SHORT).show();
                });

        db.collection("bankSettings")
                .document("profitRate")
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        profitRate = doc.getDouble("rate");
                        tvProfitableRate.setText(profitRate + "% / năm");
                        loadedRate = true;
                        triggerProfitCalc();
                    } else {
                        Toast.makeText(this, "Không tìm thấy lãi suất", Toast.LENGTH_SHORT).show();
                        tvProfitableRate.setText("0% / năm");
                    }
                });

    }

    private void triggerProfitCalc() {
        if (loadedBalance && loadedRate)
            calculateMonthlyProfit();
    }


    private void calculateMonthlyProfit() {
        if (currentBalance <= 0 || profitRate <= 0) {
            tvProfit.setText("0");
            Toast.makeText(this, "Tài khoản không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int monthNow = new Date().getMonth() + 1;
        int yearNow = new Date().getYear() + 1900;

        Log.d("SavingAccountActivity", "calculateMonthlyProfit: " + monthNow + "/" + yearNow);
        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (!doc.exists()) return;

                    int lastMonth = doc.getLong("lastProfitMonth") != null
                            ? doc.getLong("lastProfitMonth").intValue() : -1;

                    int lastYear = doc.getLong("lastProfitYear") != null
                            ? doc.getLong("lastProfitYear").intValue() : -1;

                    // Nếu đã tính trong tháng này, chỉ hiển thị
                    if (lastMonth == monthNow && lastYear == yearNow) {
                        double savedProfit = doc.getDouble("amountProfit") != null
                                ? doc.getDouble("amountProfit") : 0;
                        tvProfit.setText(String.format("%,.0f", savedProfit));
                        return;
                    }

                    // Chưa tính tháng này, tính mới
                    double profitMonth = currentBalance * (profitRate / 100 / 12);
                    double rounded = Math.round(profitMonth);

                    tvProfit.setText(String.format("%,.0f", rounded));

                    // Cập nhật vào Firestore
                    db.collection("users")
                            .document(uid)
                            .collection("accounts")
                            .document(accountId)
                            .update(
                                    "lastProfitMonth", monthNow,
                                    "lastProfitYear", yearNow,
                                    "amountProfit", rounded,
                                    "balance", currentBalance + rounded
                            );

                    tvBalance.setText(String.format("%,.0f", currentBalance + rounded));

                    // Thêm vào lịch sử
                    db.collection("transactions")
                            .add(new Transaction(doc.getId(), accountId, rounded, "", "Lãi suất tháng " + monthNow + "/" + yearNow , "paid", tvAccountNumber.getText().toString(), "", "", new Date()));

                });
    }

    private void loadTransactionHistory() {
        if (accountId == null) return;

        db.collection("transactions")
                .whereEqualTo("accountId", accountId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snap, e) -> {

                    if (e != null) {
                        Log.e("Firestore", "loadTransactionHistory error", e);
                        return;
                    }

                    if (snap == null) return;   // tránh crash

                    list = new ArrayList<>();
                    for (DocumentSnapshot doc : snap) {
                        String description = doc.getString("description");
                        Double amount = doc.getDouble("amount");
                        Date timestamp = doc.getDate("timestamp");
                        String bankId = doc.contains("bankId") ? doc.getString("bankId") : null;
                        String status = doc.contains("status") ? doc.getString("status") : null;
                        String toAccountNumber = doc.contains("toAccountNumber") ? doc.getString("toAccountNumber") : null;
                        String toFullname = doc.contains("toFullname") ? doc.getString("toFullname") : null;
                        String type = doc.contains("type") ? doc.getString("type") : null;

                        Transaction t = new Transaction(doc.getId(), accountId, amount, bankId, description , status, toAccountNumber, toFullname, type, timestamp);
                        list.add(t);
                    }
                    adapter.setData(list);
                });
    }


}
