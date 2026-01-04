package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.Bill;
import com.example.project.Model.Electricity;
import com.example.project.R;
import com.example.project.utils.ElectricitySupplierAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PayElectricityActivity extends AppCompatActivity {

    private AutoCompleteTextView edtElectricity;
    private EditText edtCustomerCode;
    private Button btnTransaction;
    private ImageView btnBack;
    private TextInputEditText tvPeriod, tvAmount, tvContent;
    private TextInputLayout layoutAmount, layoutPeriod;
    private TextView tvBalance, tvAccountNumber;

    private FirebaseFirestore db;

    private String selectedProviderCode;
    private Bill currentBill;

    private String uid;
    private String paymentAccountId;
    private long paymentBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity_bill);

        // ===== INIT VIEW =====
        edtElectricity = findViewById(R.id.edtElectriccity);
        edtCustomerCode = findViewById(R.id.edtCustomerCode);
        tvAmount = findViewById(R.id.tvAmount);
        tvPeriod = findViewById(R.id.tvPeriod);
        tvBalance = findViewById(R.id.tvBalance);
        tvContent = findViewById(R.id.edtContent);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        btnTransaction = findViewById(R.id.btnTransaction);
        btnBack = findViewById(R.id.btnBack);
        layoutAmount = findViewById(R.id.layoutAmount);
        layoutPeriod = findViewById(R.id.layoutPeriod);

        db = FirebaseFirestore.getInstance();
        uid = UserSession.getUid();

        btnBack.setOnClickListener(v -> finish());

        btnTransaction.setEnabled(false);
        layoutAmount.setVisibility(View.GONE);
        layoutPeriod.setVisibility(View.GONE);

        loadElectricitySupplier();
        loadPaymentAccount();

        // Khi nhập xong mã KH → kiểm tra
        edtCustomerCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkCustomerAndBill();
            }
        });

        btnTransaction.setOnClickListener(v -> goVerifyPIN());


    }

    /* =====================================================
     * LOAD TÀI KHOẢN THANH TOÁN
     * ===================================================== */
    private void loadPaymentAccount() {
        db.collection("users").document(uid)
                .collection("accounts")
                .whereEqualTo("accountType", "Thanh toán")
                .limit(1)
                .get()
                .addOnSuccessListener(snap -> {
                    if (snap.isEmpty()) {
                        showDialog("Thông báo", "Không có tài khoản thanh toán");
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

    /* =====================================================
     * LOAD NHÀ CUNG CẤP ĐIỆN
     * ===================================================== */
    private void loadElectricitySupplier() {
        db.collection("electricitySupplier")
                .get()
                .addOnSuccessListener(query -> {
                    List<Electricity> list = new ArrayList<>();

                    for (DocumentSnapshot doc : query.getDocuments()) {
                        Electricity e = doc.toObject(Electricity.class);
                        if (e != null) {
                            e.setId(doc.getId());
                            list.add(e);
                        }
                    }

                    ElectricitySupplierAdapter adapter =
                            new ElectricitySupplierAdapter(this, list);
                    edtElectricity.setAdapter(adapter);

                    edtElectricity.setOnItemClickListener((p, v, pos, id) ->
                            selectedProviderCode = list.get(pos).getCode()
                    );
                });
    }

    /* =====================================================
     * KIỂM TRA MÃ KH + HÓA ĐƠN
     * ===================================================== */
    private void checkCustomerAndBill() {
        String customerCode = edtCustomerCode.getText().toString().trim();

        if (TextUtils.isEmpty(customerCode)) {
            showDialog("Thông báo", "Vui lòng nhập mã số khách hàng");
            resetBillUI();
            return;
        }

        if (selectedProviderCode == null) {
            showDialog("Thông báo", "Vui lòng chọn nhà cung cấp điện");
            resetBillUI();
            return;
        }

        // 1️⃣ Kiểm tra mã KH có tồn tại không
        db.collection("bills")
                .whereEqualTo("customerCode", customerCode)
                .whereEqualTo("providerCode", selectedProviderCode)
                .limit(1)
                .get()
                .addOnSuccessListener(customerQuery -> {
                    if (customerQuery.isEmpty()) {
                        showDialog("Thông báo", "Mã khách hàng không hợp lệ");
                        resetBillUI();
                    } else {
                        // 2️⃣ Có KH → kiểm tra hóa đơn
                        checkElectricityBill(customerCode);
                    }
                });
    }

    private void checkElectricityBill(String customerCode) {
        db.collection("bills")
                .whereEqualTo("providerCode", selectedProviderCode)
                .whereEqualTo("customerCode", customerCode)
                .whereEqualTo("status", "unpaid")
                .whereEqualTo("type", "ELECTRICITY")
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    String contentDefaut = customerCode + " thanh toán hóa đơn điện";
                    tvContent.setText(contentDefaut);
                    if (query.isEmpty()) {
                        showNoBillDialog();
                    } else {
                        DocumentSnapshot doc = query.getDocuments().get(0);
                        currentBill = doc.toObject(Bill.class);
                        if (currentBill != null) {
                            currentBill.setId(doc.getId());
                            showBillInfo();
                        }
                    }
                });
    }

    /* =====================================================
     * HIỂN THỊ HÓA ĐƠN
     * ===================================================== */
    private void showBillInfo() {
        tvPeriod.setText(currentBill.getPeriod());
        tvAmount.setText(formatMoney(currentBill.getAmount()));

        layoutAmount.setVisibility(View.VISIBLE);
        layoutPeriod.setVisibility(View.VISIBLE);
        btnTransaction.setEnabled(true);
    }

    private void showNoBillDialog() {
        resetBillUI();
        showDialog("Thông báo", "Bạn không có hóa đơn tiền điện cần thanh toán");
    }

    private void resetBillUI() {
        currentBill = null;
        btnTransaction.setEnabled(false);
        layoutAmount.setVisibility(View.GONE);
        layoutPeriod.setVisibility(View.GONE);
        tvAmount.setText("");
        tvPeriod.setText("");
    }

    /* =====================================================
     * VERIFY PIN
     * ===================================================== */
    private void goVerifyPIN() {
        if (currentBill == null) return;
        String content = tvContent.getText().toString().trim();
        String amountText = tvAmount.getText().toString().trim();
        int amount = Integer.parseInt(amountText.replace(".", ""));



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
                            intent.putExtra("email", email);
                            intent.putExtra("name", name);


        intent.putExtra("type", "ELECTRICITY");
        intent.putExtra("billId", currentBill.getId());
        intent.putExtra("amount", amount);
        intent.putExtra("accountNumber", tvAccountNumber.getText().toString());
        intent.putExtra("providerCode", currentBill.getProviderCode());
        intent.putExtra("customerCode", currentBill.getCustomerCode());
        intent.putExtra("description",  content);
        intent.putExtra("paymentAccountId", paymentAccountId);
        startActivity(intent);
    });
    }

    /* =====================================================
     * UTIL
     * ===================================================== */
    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private String formatMoney(long money) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(money).replace(",", ".");
    }
}
