package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.project.Model.Bank;
import com.example.project.Model.Transaction;

import com.example.project.R;
import com.example.project.utils.BankAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransferMoneyActivity extends AppCompatActivity {

    private TextView tvAccountNumber, tvBalance, tvRecipientName;
    private AutoCompleteTextView edtBank;
    private TextInputEditText edtAccountRecipient, edtAmount, edtContent;
    private Button btnTransaction;
    private ImageView buttonBack;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String selectedBankCode = null;   // BankId sau khi chọn
    private String recipientUserId;
    private String senderName = "";


// userId người nhận

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        tvRecipientName = findViewById(R.id.tvRecipientName);
        btnTransaction = findViewById(R.id.btnTransaction);

        edtBank = findViewById(R.id.edtBank);
        edtAccountRecipient = findViewById(R.id.edtAccountRecipient);
        edtAmount = findViewById(R.id.edtAmount);
        edtContent = findViewById(R.id.edtContent);
        buttonBack = findViewById(R.id.btnBack);

        buttonBack.setOnClickListener(v -> finish());






        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // --- Lấy dữ liệu tài khoản gửi ---

        tvAccountNumber.setText(getIntent().getStringExtra("accountNumber"));
        loadUserBalance(); // luôn load từ Firestore


        loadListBank();

        // Khi rời khỏi ô nhập số tài khoản → kiểm tra tài khoản + ngân hàng
        edtAccountRecipient.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) validateRecipientAccount();
        });

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




        btnTransaction.setOnClickListener(v -> processTransfer());

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {
                    if (userDoc.exists()) {


                        senderName = userDoc.getString("name");

                        // Gán nội dung mặc định
                        String defaultContent = senderName + " chuyen tien" ;
                        edtContent.setText(defaultContent);


                    }
                });



    }

    /** -----------------------------------------------------------
     *  LOAD DANH SÁCH NGÂN HÀNG
     * -----------------------------------------------------------*/
    private void loadListBank() {
        db.collection("banks")
                .get()
                .addOnSuccessListener(query -> {
                    List<Bank> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        Bank b = doc.toObject(Bank.class);
                        if (b != null) list.add(b);
                    }

                    BankAdapter adapter = new BankAdapter(this, list);
                    edtBank.setAdapter(adapter);

                    edtBank.setOnItemClickListener((parent, view, position, id) -> {
                        Bank bank = (Bank) parent.getItemAtPosition(position);
                        selectedBankCode = bank.getCode();   // Lưu bankId chọn
                    });
                });
    }

    /** -----------------------------------------------------------
     *  KIỂM TRA TÀI KHOẢN NGƯỜI NHẬN + ĐÚNG NGÂN HÀNG
     * -----------------------------------------------------------*/
    private void validateRecipientAccount() {
        String acc = edtAccountRecipient.getText().toString().trim();

        if (acc.isEmpty() || selectedBankCode == null) {
            Toast.makeText(TransferMoneyActivity.this, "Vui lòng nhập tài khoản và chọn ngân hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy tất cả users (nếu user nhiều, có thể cần tối ưu)
        db.collection("users")
                .get()
                .addOnSuccessListener(usersQuery -> {
                    boolean found = false;

                    for (DocumentSnapshot userDoc : usersQuery.getDocuments()) {
                        String userId = userDoc.getId();

                        // Truy vấn subcollection accounts
                        db.collection("users")
                                .document(userId)
                                .collection("accounts")
                                .whereEqualTo("accountNumber", acc)
                                .limit(1)
                                .get()
                                .addOnSuccessListener(accQuery -> {
                                    if (!accQuery.isEmpty()) {
                                        DocumentSnapshot accDoc = accQuery.getDocuments().get(0);
                                        String bankCodeInDb = accDoc.getString("bankId");


                                        // Kiểm tra ngân hàng
                                        if (!selectedBankCode.equals(bankCodeInDb)) {
                                            edtAccountRecipient.setError("Sai ngân hàng đã chọn!");
                                            tvRecipientName.setText("");
                                            Toast.makeText(TransferMoneyActivity.this, "Sai ngân hàng đã chọn", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        // Kiểm tra không chuyển cho chính mình
                                        if (acc.equals(tvAccountNumber.getText().toString())) {
                                            edtAccountRecipient.setError("Không thể chuyển cho chính bạn!");
                                            tvRecipientName.setText("");
                                            Toast.makeText(TransferMoneyActivity.this, "Không thể chuyển cho chính bạn!", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // Lấy tên chủ tài khoản
                                        String ownerName = userDoc.getString("name");
                                        tvRecipientName.setText("👤 " + ownerName);
                                        recipientUserId = userId;
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(TransferMoneyActivity.this, "Lỗi khi kiểm tra account: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TransferMoneyActivity.this, "Lỗi khi lấy danh sách user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserBalance();
    }


    private void loadUserBalance() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        String accountNo = tvAccountNumber.getText().toString();

        db.collection("users").document(uid)
                .collection("accounts")
                .whereEqualTo("accountNumber", accountNo)
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        DocumentSnapshot doc = query.getDocuments().get(0);

                        Double balance = doc.getDouble("balance");
                        if (balance != null) {
                            tvBalance.setText(String.format("%,.0f", balance));

                        }
                    }
                });
    }




    /** -----------------------------------------------------------
     *  XỬ LÝ CHUYỂN TIỀN
     * -----------------------------------------------------------*/
    private void processTransfer() {
        String recipientAcc = edtAccountRecipient.getText().toString().trim();
        String amountStr = edtAmount.getText().toString().trim();
        String content = edtContent.getText().toString().trim();


        if (recipientAcc.isEmpty() || amountStr.isEmpty() || selectedBankCode == null) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int amount = Integer.parseInt(amountStr.replace(".", ""));

        long senderBalance = Long.parseLong(
                tvBalance.getText().toString().replace(".", "").replace(",", "").trim()
        );


        if (amount < 10000) {
            Toast.makeText(this, "Số tiền không hợp lệ! Số tiền không được bé hơn 10.000đ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senderBalance < amount) {
            Toast.makeText(this, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (recipientUserId == null) {
            Toast.makeText(this, "Không tìm thấy người nhận, hãy kiểm tra lại thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy số điện thoại người gửi từ Firestore
        db.collection("users").document(mAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(userDoc -> {
                    String phone = userDoc.getString("phone"); // số điện thoại đăng ký Firebase
                    if (phone != null && !phone.isEmpty()) {
                        // Chuyển sang OTPVerificationActivity
                        Intent intent = new Intent(TransferMoneyActivity.this, VerifyPINActivity.class);
                        intent.putExtra("amount", amount);
                        intent.putExtra("name", userDoc.getString("name"));
                        intent.putExtra("email", userDoc.getString("email"));
                        intent.putExtra("accountNumber", tvAccountNumber.getText().toString());
                        intent.putExtra("toFullname", tvRecipientName.getText().toString().replace("👤 ", ""));
                        intent.putExtra("toAccountNumber", recipientAcc);
                        intent.putExtra("description", content);
                        intent.putExtra("bankId", selectedBankCode);
                        intent.putExtra("phone", phone);
                        intent.putExtra("type", "TRANSFER_MONEY");
                        startActivity(intent);
                    }
                });
    }

}
