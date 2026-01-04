package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;

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

import java.text.SimpleDateFormat;
import java.util.*;

public class MortgageAccountActivity extends AppCompatActivity {
    private TextView tvAccountNumber, tvBalance, tvAmountLoan, tvPayMoneyMonth, tvPayMoneyTwoWeek;
    private ImageView btnBack;
    private Button btnPay;
    private EditText etSearch;
    private LinearLayout containerMonth, containerTwoWeek, containerTopUp, containerWithdraw;
    private TransactionAdapter adapter;
    private String accountId, uid, paymentMode;
    private FirebaseFirestore db;
    private List<Transaction> list;
    private Double amountLoan;
    private Integer loanTermYears;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mortgage_account);

        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        tvPayMoneyMonth = findViewById(R.id.tvPayMoneyMonth);
        tvPayMoneyTwoWeek = findViewById(R.id.tvPayMoneyTwoWeek);
        tvAmountLoan = findViewById(R.id.tvAmountLoan);
        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        containerMonth = findViewById(R.id.containerMonth);
        containerTwoWeek = findViewById(R.id.containerTwoWeek);
        containerTopUp = findViewById(R.id.containerTopUp);
        containerWithdraw = findViewById(R.id.containerWithdraw);



        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();
        accountId = getIntent().getStringExtra("accountId");

        RecyclerView recycler = findViewById(R.id.recyclerTransactions);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TransactionAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        containerTopUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, MortgageTopUpActivity.class);
            intent.putExtra("mortgageAccountId", accountId);
            startActivity(intent);
        });


        containerWithdraw.setOnClickListener(v -> {
            Intent intent = new Intent(this, MortgageWithDrawActivity.class);
            intent.putExtra("mortgageAccountId", accountId);
            startActivity(intent);
        });


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
                    if (doc.exists()) {
                        String number = doc.getString("accountNumber");
                        Double bal = doc.getDouble("balance");
                        amountLoan = doc.getDouble("amountLoan");
                        loanTermYears = doc.getLong("loanTermYears").intValue();
                        paymentMode = doc.getString("paymentMode");

                        tvAccountNumber.setText(number);
                        tvBalance.setText(String.format("%,.0f", bal));
                        tvAmountLoan.setText(String.format("%,.0f", amountLoan));

                        loadMortgagePayments();
                        checkPaymentPeriod();
                        loadTransactionHistory();
                    } else {
                        Toast.makeText(this, "Không tìm thấy tài khoản thanh toán", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải tài khoản!", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadMortgagePayments() {
        double payment = getRequiredPayment();
        if (paymentMode.equals("monthly")) {
            tvPayMoneyMonth.setText(String.format("%,.0f VNĐ", payment));
            containerTwoWeek.setVisibility(View.GONE);
        } else {
            tvPayMoneyTwoWeek.setText(String.format("%,.0f VNĐ", payment));
            containerMonth.setVisibility(View.GONE);
        }
    }

    private String getCurrentPeriodId() {
        if (paymentMode.equals("monthly")) {
            return new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        } else {
            // biweekly theo tuần ISO
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-ww", Locale.getDefault());
            return sdf.format(new Date()); // yyyy-ww
        }
    }

    private double getRequiredPayment() {
        double P = amountLoan;
        double monthlyRate = 10.0 / 12.0 / 100.0;
        int n = loanTermYears * 12;

        double monthlyPayment =
                P * (monthlyRate * Math.pow(1 + monthlyRate, n))
                        / (Math.pow(1 + monthlyRate, n) - 1);

        if (paymentMode.equals("monthly")) {
            return monthlyPayment;
        } else {
            return (monthlyPayment * 12) / 26.0;  // biweekly
        }
    }

    private void checkPaymentPeriod() {
        String currentPeriodId = getCurrentPeriodId();
        double requiredPayment = getRequiredPayment();

        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId)
                .collection("payments")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(snap -> {

                    // Chưa có kỳ nào thì tạo luôn kỳ đầu
                    if (snap.isEmpty()) {
                        createNewPaymentRecord(currentPeriodId, requiredPayment);
                        return;
                    }

                    DocumentSnapshot lastPeriod = snap.getDocuments().get(0);
                    String lastPeriodId = lastPeriod.getId();
                    Boolean paid = lastPeriod.getBoolean("paid");
                    Boolean debtAdded = lastPeriod.getBoolean("debtAdded");

                    if (debtAdded == null) debtAdded = false;
                    if (paid == null) paid = false;

                    // Nếu kỳ hiện tại trùng kỳ cuối thì không làm gì
                    if (lastPeriodId.equals(currentPeriodId)) return;

                    // Đã qua kỳ mới
                    if (!paid) {
                        if (!debtAdded) {
                            // Kỳ trước chưa thanh toán thì cộng nợ vào gốc
                            double missedAmount = lastPeriod.getDouble("requiredPayment");
                            addMissedPaymentToLoan(missedAmount);
                            lastPeriod.getReference().update("debtAdded", true);
                        }
                    }

                    // Tạo kỳ mới
                    createNewPaymentRecord(currentPeriodId, requiredPayment);
                });
    }

    private void createNewPaymentRecord(String periodId, double requiredPayment) {
        Map<String, Object> data = new HashMap<>();
        data.put("requiredPayment", requiredPayment);
        data.put("paid", false);
        data.put("paidAmount", 0);
        data.put("status", "pending");
        data.put("createdAt", new Date());

        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId)
                .collection("payments")
                .document(periodId)
                .set(data);
    }

    private void addMissedPaymentToLoan(double missedAmount) {
        double newLoan = amountLoan + missedAmount;

        db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(accountId)
                .update("amountLoan", newLoan)
                .addOnSuccessListener(a -> {

                    Map<String, Object> t = new HashMap<>();
                    t.put("accountId", accountId);
                    t.put("amount", missedAmount);
                    t.put("description", "Cộng dồn nợ do bỏ lỡ kỳ thanh toán trước");
                    t.put("timestamp", new Date());
                    t.put("status", "missed");

                    db.collection("transactions").add(t);
                    tvAmountLoan.setText(String.format("%,.0f", newLoan));

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

                    if (snap == null) return;

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
