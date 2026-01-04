package com.example.project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Model.Transaction;
import com.example.project.R;
import com.example.project.utils.EmailSender;
import com.example.project.utils.NotificationReceiver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VerifyOTPActivity extends AppCompatActivity {

    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView txtTimer, txtResend;
    private Button btnVerify;

    private FirebaseFirestore db;

    private int resendTime = 300;
    private CountDownTimer countDownTimer;

    private int amount;
    private String senderName, senderAcc, recipientName, recipientAcc, content, bankId, email;
    private String currentOTP;

    private String accountId;
    private String type;
    private String billId;
    private String providerCode;
    private String customerCode;
    private String paymentAccountId;
    private String mortgageAccountId;
    private String savingAccountId;

    private String uid;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);
        otp6 = findViewById(R.id.otp6);
        btnVerify = findViewById(R.id.btnVerify);
        txtResend = findViewById(R.id.txtResend);
        txtTimer = findViewById(R.id.txtTimer);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        amount = intent.getIntExtra("amount", 0);
        senderName = intent.getStringExtra("name");
        senderAcc = intent.getStringExtra("accountNumber");
        recipientName = intent.getStringExtra("toFullname");
        recipientAcc = intent.getStringExtra("toAccountNumber");
        content = intent.getStringExtra("description");
        bankId = intent.getStringExtra("bankId");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        type = intent.getStringExtra("type");
        billId = intent.getStringExtra("billId");
        providerCode = intent.getStringExtra("providerCode");
        customerCode = intent.getStringExtra("customerCode");
        paymentAccountId = intent.getStringExtra("paymentAccountId");
        mortgageAccountId = intent.getStringExtra("mortgageAccountId");
        savingAccountId = intent.getStringExtra("savingAccountId");





        sendOTP(email);
        setupOTPInputs();
        startResendTimer();
        requestNotificationPermission();

        btnVerify.setOnClickListener(v -> verifyOTP());
        txtResend.setOnClickListener(v -> sendOTP(email));
    }

    private void startResendTimer() {
        txtResend.setEnabled(false);
        if (countDownTimer != null) countDownTimer.cancel();

        countDownTimer = new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String formatted = String.format("%02d:%02d", seconds / 60, seconds % 60);
                txtTimer.setText("⏳ " + formatted);
                txtResend.setText("Gửi lại mã (" + seconds + "s)");
            }
            @Override
            public void onFinish() {
                txtResend.setEnabled(true);
                txtResend.setOnClickListener(v -> {
                    sendOTP(email);
                    resendTime = 300;
                    startResendTimer();
                });
            }
        }.start();
    }

    private void sendOTP(String email) {
        currentOTP = EmailSender.generateOTP();
        EmailSender.sendEmail(email, currentOTP);
        Toast.makeText(this, "OTP đã gửi tới email!", Toast.LENGTH_SHORT).show();
    }

    private void setupOTPInputs() {
        EditText[] otps = {otp1, otp2, otp3, otp4, otp5, otp6};
        for (int i = 0; i < otps.length; i++) {
            final int index = i;
            otps[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1 && index < otps.length - 1) {
                        otps[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        otps[index - 1].requestFocus();
                        otps[index - 1].setSelection(otps[index - 1].getText().length());
                    }
                }
            });
        }
    }

    private void verifyOTP() {
        String code = otp1.getText().toString().trim() +
                otp2.getText().toString().trim() +
                otp3.getText().toString().trim() +
                otp4.getText().toString().trim() +
                otp5.getText().toString().trim() +
                otp6.getText().toString().trim();

        if (code.length() < 6) {
            Toast.makeText(this, "Vui lòng nhập đủ 6 chữ số OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!code.equals(currentOTP)) {
            Toast.makeText(this, "OTP không đúng!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Xác thực OTP thành công!", Toast.LENGTH_SHORT).show();
        if (type.equals("TRANSFER_MONEY")) {
            processTransaction();

        } else if (type.equals("MORTGAGE_TOPUP")) {
            processMortgageTopUp();

        } else if (type.equals("MORTGAGE_WITHDRAW")) {
            processMortgageWithdraw();

        }else if (type.equals("SAVING_TOPUP")) {
            processSavingTopUp();

        } else if (type.equals("SAVING_WITHDRAW")){
            processSavingWithdraw();
        }else if (type.equals("PHONE_DEPOSIT")) {
            processPhoneDeposit();
        }else if(type.equals("ELECTRICITY"))
            processElectricity();
        else if(type.equals("WATER"))
            processWater();



    }

    // TRANSFER
    private void processTransaction() {
        db.collection("users").get().addOnSuccessListener(users -> {
            for (DocumentSnapshot userDoc : users) {
                userDoc.getReference()
                        .collection("accounts")
                        .whereEqualTo("accountNumber", senderAcc)
                        .limit(1)
                        .get().addOnSuccessListener(accQuery -> {

                            if (accQuery.isEmpty()) return;

                            DocumentSnapshot accDoc = accQuery.getDocuments().get(0);
                            accountId = accDoc.getId();

                            long currentBalance = accDoc.getLong("balance");
                            long newBalance = currentBalance - amount;

                            accDoc.getReference().update("balance", newBalance)
                                    .addOnSuccessListener(v -> {
                                        sendSenderNotification(newBalance, type);
                                        findRecipientAccount(newBalance);
                                    });
                        });
            }
        });
    }

    // MORTGAGE_TOPUP
    private void processMortgageTopUp() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference mortgageRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(mortgageAccountId);

        db.runTransaction(transaction -> {

            DocumentSnapshot paymentSnap = transaction.get(paymentRef);
            DocumentSnapshot mortgageSnap = transaction.get(mortgageRef);

            long paymentBalance = paymentSnap.getLong("balance");
            long mortgageBalance = mortgageSnap.getLong("balance");

            if (paymentBalance < amount) {
                throw new RuntimeException("Không đủ tiền trong tài khoản thanh toán");
            }

            transaction.update(paymentRef, "balance", paymentBalance - amount);
            transaction.update(mortgageRef, "balance", mortgageBalance + amount);

            return paymentBalance - amount;

        }).addOnSuccessListener(newPaymentBalance -> {

            createMortgageTransaction("TOPUP");

            // thông báo biến động số dư TK thanh toán


            sendSenderNotification(newPaymentBalance, "MORTGAGE_TOPUP");

            openSuccessActivity(newPaymentBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

// MORTGAGE_WITHDRAW
    private void processMortgageWithdraw() {
        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference mortgageRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(mortgageAccountId);

        db.runTransaction(transaction -> {

            DocumentSnapshot mortgageSnap = transaction.get(mortgageRef);
            DocumentSnapshot paymentSnap = transaction.get(paymentRef);

            long mortgageBalance = mortgageSnap.getLong("balance");
            long paymentBalance = paymentSnap.getLong("balance");

            if (mortgageBalance < amount) {
                throw new RuntimeException("Khoản vay không đủ để rút");
            }

            transaction.update(mortgageRef, "balance", mortgageBalance - amount);
            transaction.update(paymentRef, "balance", paymentBalance + amount);

            return paymentBalance + amount;

        }).addOnSuccessListener(newPaymentBalance -> {

            createMortgageTransaction("WITHDRAW");

            sendSenderNotification(newPaymentBalance, "MORTGAGE_WITHDRAW");

            openSuccessActivity(newPaymentBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

// TẠO GIAO DỊCH NẠP RÚT VÀO TÀI KHOẢN VAY
    private void createMortgageTransaction(String action) {
        Transaction t = new Transaction();
        t.setId(null);
        t.setAccountId(mortgageAccountId);

        // NẠP: +amount | RÚT: -amount
        t.setAmount(action.equals("TOPUP") ? amount : -amount);

        t.setType(action); // TOPUP | WITHDRAW
        t.setStatus("paid");
        t.setBankId(bankId);

        t.setDescription(content != null && !content.isEmpty()
                ? content
                : (action.equals("TOPUP")
                ? "Nạp tiền vào tài khoản vay"
                : "Rút tiền từ tài khoản vay")
        );

        t.setTimestamp(new Date());

        db.collection("transactions").add(t);
    }


    // SAVING_TOPUP
    private void processSavingTopUp() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference savingRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(savingAccountId);

        db.runTransaction(transaction -> {

            DocumentSnapshot paymentSnap = transaction.get(paymentRef);
            DocumentSnapshot savingSnap = transaction.get(savingRef);

            long paymentBalance = paymentSnap.getLong("balance");
            long savingBalance = savingSnap.getLong("balance");

            if (paymentBalance < amount) {
                throw new RuntimeException("Không đủ tiền trong tài khoản thanh toán");
            }

            transaction.update(paymentRef, "balance", paymentBalance - amount);
            transaction.update(savingRef, "balance", savingBalance + amount);

            return paymentBalance - amount;

        }).addOnSuccessListener(newPaymentBalance -> {

            createSavingTransaction("TOPUP");

            sendSenderNotification(newPaymentBalance, "SAVING_TOPUP");

            openSuccessActivity(newPaymentBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

    // SAVING_WITHDRAW

    private void processSavingWithdraw() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference savingRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(savingAccountId);

        db.runTransaction(transaction -> {

            DocumentSnapshot savingSnap = transaction.get(savingRef);
            DocumentSnapshot paymentSnap = transaction.get(paymentRef);

            long savingBalance = savingSnap.getLong("balance");
            long paymentBalance = paymentSnap.getLong("balance");

            if (savingBalance < amount) {
                throw new RuntimeException("Tài khoản tiết kiệm không đủ để rút");
            }

            transaction.update(savingRef, "balance", savingBalance - amount);
            transaction.update(paymentRef, "balance", paymentBalance + amount);

            return paymentBalance + amount;

        }).addOnSuccessListener(newPaymentBalance -> {

            createSavingTransaction("WITHDRAW");


            // 🔔 thông báo biến động số dư TK thanh toán
            sendSenderNotification(newPaymentBalance, "SAVING_WITHDRAW");
            openSuccessActivity(newPaymentBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }


    // NẠP TIỀN ĐIỆN THOẠI

    // ================== PHONE DEPOSIT ==================
    private void processPhoneDeposit() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        db.runTransaction(transaction -> {

            DocumentSnapshot paymentSnap = transaction.get(paymentRef);
            long paymentBalance = paymentSnap.getLong("balance");

            if (paymentBalance < amount) {
                throw new RuntimeException("Số dư không đủ để nạp tiền điện thoại");
            }

            // trừ tiền
            transaction.update(paymentRef, "balance", paymentBalance - amount);

            return paymentBalance - amount;

        }).addOnSuccessListener(newBalance -> {

            createPhoneDepositTransaction();

            // 🔔 THÔNG BÁO BIẾN ĐỘNG SỐ DƯ
            sendSenderNotification(newBalance, "PHONE_DEPOSIT");

            openSuccessActivity(newBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

    // THANH TOÁN TIỀN ĐIỆN
    // ================= ELECTRICITY =================
    private void processElectricity() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference billRef = db.collection("bills")
                .document(billId);

        db.runTransaction(transaction -> {

            DocumentSnapshot paymentSnap = transaction.get(paymentRef);
            DocumentSnapshot billSnap = transaction.get(billRef);

            long balance = paymentSnap.getLong("balance");
            long billAmount = billSnap.getLong("amount");

            if (balance < billAmount) {
                throw new RuntimeException("Số dư không đủ để thanh toán tiền điện");
            }

            // 1️⃣ Trừ tiền
            transaction.update(paymentRef, "balance", balance - billAmount);

            // 2️⃣ Update hóa đơn
            transaction.update(billRef,
                    "status", "paid",
                    "paidAt", new Date()
            );

            return balance - billAmount;

        }).addOnSuccessListener(newBalance -> {
            createElectricityTransaction();

            sendSenderNotification(newBalance, "ELECTRICITY");

            openSuccessActivity(newBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }


    // THANH TOÁN TIỀN NƯỚC
    // ================= WATER =================
    private void processWater() {

        DocumentReference paymentRef = db.collection("users")
                .document(uid)
                .collection("accounts")
                .document(paymentAccountId);

        DocumentReference billRef = db.collection("bills")
                .document(billId);

        db.runTransaction(transaction -> {

            DocumentSnapshot paymentSnap = transaction.get(paymentRef);
            DocumentSnapshot billSnap = transaction.get(billRef);

            long balance = paymentSnap.getLong("balance");
            long billAmount = billSnap.getLong("amount");

            if (balance < billAmount) {
                throw new RuntimeException("Số dư không đủ để thanh toán tiền nước");
            }

            // 1️⃣ Trừ tiền
            transaction.update(paymentRef, "balance", balance - billAmount);

            // 2️⃣ Cập nhật hóa đơn
            transaction.update(billRef,
                    "status", "paid",
                    "paidAt", new Date()
            );

            return balance - billAmount;

        }).addOnSuccessListener(newBalance -> {
            createWaterTransaction();

            sendSenderNotification(newBalance, "WATER");

            openSuccessActivity(newBalance);

        }).addOnFailureListener(e ->
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show()
        );
    }

    // TẠO GIAO DỊCH NẠP TIỀN ĐIỆN THOẠI
    private void createPhoneDepositTransaction() {

        Transaction t = new Transaction();
        t.setId(null);
        t.setAccountId(paymentAccountId);
        t.setAmount(-amount); // trừ tiền
        t.setType("PHONE_DEPOSIT");
        t.setStatus("paid");
        t.setBankId(bankId);
        t.setToAccountNumber(recipientAcc); // SỐ ĐIỆN THOẠI
        t.setToFullname("Nạp tiền điện thoại");

        t.setDescription(
                content != null && !content.isEmpty()
                        ? content
                        : "Nạp tiền điện thoại " + recipientAcc
        );

        t.setTimestamp(new Date());

        db.collection("transactions").add(t);
    }



    // TẠO GIAO DỊCH NẠP RÚT TÀI KHOẢN TIẾT KIỆM
    private void createSavingTransaction(String action) {

        Transaction t = new Transaction();
        t.setId(null);
        t.setAccountId(savingAccountId);

        // NẠP: +amount | RÚT: -amount
        t.setAmount(action.equals("TOPUP") ? amount : -amount);

        t.setType(action); // TOPUP | WITHDRAW
        t.setStatus("paid");
        t.setBankId(bankId);

        t.setDescription(content != null && !content.isEmpty()
                ? content
                : (action.equals("TOPUP")
                ? "Nạp tiền vào tài khoản tiết kiệm"
                : "Rút tiền từ tài khoản tiết kiệm")
        );

        t.setTimestamp(new Date());

        db.collection("transactions").add(t);
    }

    // TẠO GIAO DỊCH THANH TOÁN ĐIỆN
    private void createElectricityTransaction() {

        Transaction t = new Transaction();
        t.setId(null);
        t.setAccountId(paymentAccountId);
        t.setAmount(-amount);
        t.setType("ELECTRICITY");
        t.setStatus("paid");
        t.setBankId(bankId);

        t.setDescription("Thanh toán hóa đơn điện - Mã KH: " + customerCode);

        t.setToAccountNumber(customerCode);
        t.setToFullname("Điện lực " + providerCode);
        t.setTimestamp(new Date());

        db.collection("transactions").add(t);
    }

    // TẠO GIAO DỊCH THANH TOÁN NƯỚC
    private void createWaterTransaction() {

        Transaction t = new Transaction();
        t.setId(null);
        t.setAccountId(paymentAccountId);
        t.setAmount(-amount);
        t.setType("WATER");
        t.setStatus("paid");
        t.setBankId(bankId);

        t.setDescription("Thanh toán hóa đơn nước - Mã KH: " + customerCode);

        t.setToAccountNumber(customerCode);
        t.setToFullname("Cấp nước " + providerCode);
        t.setTimestamp(new Date());

        db.collection("transactions").add(t);
    }



    private void findRecipientAccount(long newSenderBalance) {
        db.collection("users").get().addOnSuccessListener(users -> {
            for (DocumentSnapshot userDoc : users) {
                userDoc.getReference()
                        .collection("accounts")
                        .whereEqualTo("accountNumber", recipientAcc)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(accQuery -> {

                            if (accQuery.isEmpty()) return;

                            DocumentSnapshot accDoc = accQuery.getDocuments().get(0);
                            String recAccId = accDoc.getId();

                            long recBal = accDoc.getLong("balance");
                            long newRecBal = recBal + amount;

                            // 🔑 LẤY BANK ID CỦA NGƯỜI NHẬN
                            String recipientBankId = accDoc.getString("bankId");

                            accDoc.getReference()
                                    .update("balance", newRecBal)
                                    .addOnSuccessListener(v -> {

                                        boolean isSameBank =
                                                bankId.equals(recipientBankId);

                                        // 🔔 CHỈ CÙNG NGÂN HÀNG MỚI GỬI
                                        if (isSameBank) {
                                            sendRecipientNotification(recAccId, newRecBal);
                                        }

                                        createTransaction(recAccId);
                                        openSuccessActivity(newSenderBalance);
                                    });
                        });
            }
        });
    }



// hàm tạo giao dịch thanh toán
    private void createTransaction(String recipientAccountId) {
        WriteBatch batch = db.batch();
        //gửi
        Transaction sendT = new Transaction();
        sendT.setId(null);
        sendT.setAccountId(accountId);
        sendT.setAmount(-amount);
        sendT.setBankId(bankId);
        sendT.setDescription(content);
        sendT.setStatus("paid");
        sendT.setToAccountNumber(recipientAcc);
        sendT.setToFullname(recipientName);
        sendT.setType("SEND");
        sendT.setTimestamp(new Date());

        DocumentReference sendRef = db.collection("transactions").document();
        sendT.setId(sendRef.getId());
        batch.set(sendRef, sendT);

        // nhận
        Transaction recT = new Transaction();
        recT.setId(null);
        recT.setAccountId(recipientAccountId);
        recT.setAmount(amount);
        recT.setBankId(bankId);
        recT.setDescription("Nhận tiền từ " + senderName);
        recT.setStatus("paid");
        recT.setToAccountNumber(senderAcc);
        recT.setToFullname(senderName);
        recT.setType("RECEIVE");
        recT.setTimestamp(new Date());

        DocumentReference recRef = db.collection("transactions").document();
        recT.setId(recRef.getId());
        batch.set(recRef, recT);

        batch.commit();
    }

    private String formatCurrency(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount).replace(",", ".");
    }

    // -----------------------------
    // 🔔 Gửi Broadcast để tạo Notification
    // -----------------------------
    private void sendSenderNotification(long newBalance, String transactionType) {

        Intent intent = new Intent(this, NotificationReceiver.class);

        String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());

        String amountStr = formatCurrency(amount);
        String newBalStr = formatCurrency(newBalance);

        String message = "";

        switch (transactionType) {

            case "TRANSFER_MONEY":
                message = "TK " + senderAcc +
                        " | Bạn đã chuyển: -" + amountStr + " VND" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content +
                        " | CT từ " + senderAcc + " " + senderName +
                        " đến " + recipientAcc + " " + recipientName;
                break;

            case "MORTGAGE_TOPUP":
                message = "TK " + senderAcc +
                        " | Bạn đã nạp: -" + amountStr + " VND vào tài khoản vay" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content;
                break;

            case "MORTGAGE_WITHDRAW":
                message = "TK " + senderAcc +
                        " | Bạn đã nhận: +" + amountStr + " VND từ tài khoản vay" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content;
                break;

            case "SAVING_TOPUP":
                message = "TK " + senderAcc +
                        " | Bạn đã nạp: -" + amountStr + " VND vào tài khoản tiết kiệm" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content;
                break;

            case "SAVING_WITHDRAW":
                message = "TK " + senderAcc +
                        " | Bạn đã nhận: +" + amountStr + " VND từ tài khoản tiết kiệm" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content;
                break;

            case "PHONE_DEPOSIT":
                message = "TK " + senderAcc +
                        " | Bạn đã nạp thành công: -" + amountStr + " VND" +
                        " | Số điện thoại: " + recipientAcc + " +" +  amountStr + " VND" +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND" +
                        " | Nội dung: " + content;
                break;

            case "ELECTRICITY":
                message = "TK " + senderAcc +
                        " | Thanh toán tiền điện: -" + amountStr + " VND" +
                        " | Mã KH: " + customerCode +
                        " | Nhà cung cấp: " + providerCode +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND";
                break;

            case "WATER":
                message = "TK " + senderAcc +
                        " | Thanh toán tiền nước: -" + amountStr + " VND" +
                        " | Mã KH: " + customerCode +
                        " | Nhà cung cấp: " + providerCode +
                        " | Lúc: " + dateStr +
                        " | Số dư hiện tại: " + newBalStr + " VND";
                break;


        }

        intent.setAction("ACTION_BALANCE_CHANGE");
        intent.putExtra("title", "Thông báo biến động số dư");
        intent.putExtra("message", message);

        String notiAccountId =
                transactionType.equals("TRANSFER_MONEY")
                        ? accountId
                        : paymentAccountId;

        intent.putExtra("accountId", notiAccountId);

        intent.putExtra("type", "SEND");

        // âm với nạp / chuyển – dương với rút
        long signedAmount =
                transactionType.contains("WITHDRAW") ? amount : -amount;

        intent.putExtra("amount", signedAmount);

        intent.putExtra("showNotification", true);

        sendBroadcast(intent);
    }

    private void sendRecipientNotification(String recipientAccountId, long newRecBal) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        String dateStr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());
        String amountStr = formatCurrency(amount);
        String newBalStr = formatCurrency(newRecBal);
        intent.setAction("ACTION_BALANCE_CHANGE");
        intent.putExtra("title", "Thông báo biến động số dư");
        intent.putExtra("message",  "TK " + recipientAcc + " | Bạn đã nhận: " + amountStr + " VND | Lúc " + dateStr +
                " | Số dư hiện tại: " + newBalStr + " VND | " + "Nội dung: " + content + " | CT từ " + senderAcc + " " + senderName + " đến " +  recipientAcc + " " + recipientName);
        intent.putExtra("accountId", recipientAccountId);
        intent.putExtra("type", "RECEIVE");            // SEND hoặc RECEIVE
        intent.putExtra("amount", (long) + amount);
        intent.putExtra("showNotification", false);

        sendBroadcast(intent);

    }



    private void openSuccessActivity(long balance) {
        Intent i = new Intent(this, TransactionSuccessActivity.class);
        i.putExtra("amount", amount);
        i.putExtra("senderName", senderName);
        i.putExtra("senderAcc", senderAcc);
        i.putExtra("recipientName", recipientName);
        i.putExtra("recipientAcc", recipientAcc);
        i.putExtra("transactionId", "TRANS_" + System.currentTimeMillis());
        i.putExtra("content", content);
        i.putExtra("type", type);
        i.putExtra("providerCode", providerCode);
        i.putExtra("customerCode", customerCode);
        startActivity(i);
        finish();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }
    }
}
