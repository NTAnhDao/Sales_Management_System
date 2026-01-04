package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TransactionSuccessActivity extends AppCompatActivity {

    private TextView tvAmount, tvTransferee, tvRecipient,
            tvTransactionId, tvSuccessTitle,
            tvContent, tvTime,
                tvAccountNumber, tvRecieveAccountNumber, tvRecevie;

    private Button btnNewTransaction, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_success);

        // ===== ÁNH XẠ VIEW =====
        tvAmount = findViewById(R.id.tvAmount);
        tvTransferee = findViewById(R.id.tvtransferee);
        tvRecipient = findViewById(R.id.tvRecipient);
        tvTransactionId = findViewById(R.id.tvTransactionId);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvRecevie = findViewById(R.id.tvRecive);
        tvRecieveAccountNumber = findViewById(R.id.tvReciveAccountNumber);
        tvContent = findViewById(R.id.tvContent);
        tvTime = findViewById(R.id.tvTime);
        tvSuccessTitle = findViewById(R.id.tvSuccessTitle);

        btnNewTransaction = findViewById(R.id.btnNewTransaction);
        btnHome = findViewById(R.id.btnHome);

        // ===== NHẬN DỮ LIỆU =====
        Intent intent = getIntent();

        int amount = intent.getIntExtra("amount", 0);

        String senderName = intent.getStringExtra("senderName");
        String senderAcc = intent.getStringExtra("senderAcc");
        String recipientName = intent.getStringExtra("recipientName");
        String recipientAcc = intent.getStringExtra("recipientAcc");
        String content = intent.getStringExtra("content");
        String transactionId = intent.getStringExtra("transactionId");
        String type = intent.getStringExtra("type");
        String providerCode = intent.getStringExtra("providerCode");
        String customerCode = intent.getStringExtra("customerCode");

        long timestamp = intent.getLongExtra("timestamp", System.currentTimeMillis());

        // ===== TIÊU ĐỀ =====
        switch (type) {
            case "TRANSFER_MONEY":
                tvSuccessTitle.setText("Chuyển khoản thành công!");
                break;

            case "MORTGAGE_TOPUP":
                tvSuccessTitle.setText("Nạp tiền thành công!");
                break;

            case "MORTGAGE_WITHDRAW":
                tvSuccessTitle.setText("Rút tiền thành công!");
                break;

            case "SAVING_TOPUP":
                tvSuccessTitle.setText("Nạp tiền thành công!");
                break;

            case "SAVING_WITHDRAW":
                tvSuccessTitle.setText("Rút tiền thành công!");
                break;

            case "PHONE_DEPOSIT":
                tvSuccessTitle.setText("Nạp tiền điện thoại thành công!");
                break;

            case "ELECTRICITY":
                tvSuccessTitle.setText("Thanh toán tiền điện thành công!");
                break;

            case "WATER":
                tvSuccessTitle.setText("Thanh toán tiền nước thành công!");
                break;


        }

        // ===== XÁC ĐỊNH RÚT =====
        boolean isWithdraw =
                "MORTGAGE_WITHDRAW".equals(type) ||
                        "SAVING_WITHDRAW".equals(type);

        // ===== NGƯỜI CHUYỂN / NHẬN =====
        if (isWithdraw) {
            tvTransferee.setText(recipientName);
            tvAccountNumber.setText(recipientAcc);

            tvRecipient.setText(senderName);
            tvRecieveAccountNumber.setText(senderAcc);
        } else {
            tvTransferee.setText(senderName);
            tvAccountNumber.setText(senderAcc);

            tvRecipient.setText(recipientName);
            tvRecieveAccountNumber.setText(recipientAcc);
        }

        // ===== HIỂN THỊ CHUNG =====
        tvAmount.setText(String.format(Locale.getDefault(), "%,d", amount));
        tvTransactionId.setText("#" + transactionId);
        tvContent.setText(content);
        tvTime.setText(
                new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
                        .format(new Date(timestamp))
        );

        if (type.equals("ELECTRICITY") || type.equals("WATER")) {

            tvTransferee.setText(senderName);
            tvAccountNumber.setText(senderAcc);

            if (type.equals("ELECTRICITY")) {
                tvRecipient.setText("TCT Điện lực " + providerCode);
            } else {
                tvRecipient.setText("TCT Cấp Nước " + providerCode);
            }

            tvRecieveAccountNumber.setVisibility(View.GONE);
            tvRecevie.setVisibility(View.GONE);

        }



        // ===== XỬ LÝ HIỂN THỊ BUTTON =====
        if ("TRANSFER_MONEY".equals(type)) {

            btnNewTransaction.setVisibility(View.VISIBLE);
        } else {
            // ❌ Nạp / rút → ẩn nút giao dịch mới
            btnNewTransaction.setVisibility(View.GONE);
        }

        // ===== SỰ KIỆN BUTTON =====
        btnNewTransaction.setOnClickListener(v -> {
            Intent i = new Intent(
                    TransactionSuccessActivity.this,
                    TransferMoneyActivity.class
            );
            i.putExtra("accountNumber", senderAcc);
            startActivity(i);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(
                    TransactionSuccessActivity.this,
                    UserMainActivity.class
            ));
            finish();
        });
    }
}
