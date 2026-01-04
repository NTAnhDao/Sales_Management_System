package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.widget.*;

import androidx.annotation.*;
import androidx.fragment.app.Fragment;
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

public class CheckingAccountActivity extends AppCompatActivity {
    private TextView tvAccountNumber, tvBalance;
    private ImageView btnBack;
    private EditText etSearch;
    private TransactionAdapter adapter;
    private String accountId, uid;
    private FirebaseFirestore db;
    private List<Transaction> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checking_account);

        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);

        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();
        accountId = getIntent().getStringExtra("accountId");

        RecyclerView recycler = findViewById(R.id.recyclerTransactions);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TransactionAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);

        loadAccountInfo();

        btnBack.setOnClickListener(v -> finish());

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
                    if (!doc.exists()) {
                        Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Lấy accountId
                    accountId = doc.getId();
                    String number = doc.getString("accountNumber");
                    Double bal = doc.getDouble("balance");

                    tvAccountNumber.setText(number);
                    tvBalance.setText(String.format("%,.0f", bal));

                    loadTransactionHistory();

                });
    }

    private void loadTransactionHistory() {
        if (accountId == null) return;

        db.collection("transactions")
                .whereEqualTo("accountId", accountId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snap, e) -> {

                    if (e != null) {
                        Toast.makeText(this, "Lỗi tải giao dịch", Toast.LENGTH_SHORT).show();
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