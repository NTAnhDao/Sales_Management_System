package com.example.project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.Model.Transaction;
import com.example.project.R;
import com.example.project.utils.NotificationReceiver;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FaceAuthActivity extends AppCompatActivity {
    private static final String TAG = "FaceAuthActivity";
    private static final String API_KEY = "IexD3Gs2PhKw7NGawiLR66r6mSEjEAuP";
    private static final String API_SECRET = "LFVFOkemwZYToMc3C1K5cR5WxNP3BhEu";
    // Detect endpoint
    private static final String DETECT_URL = "https://api-us.faceplusplus.com/facepp/v3/detect";
    // Compare endpoint
    private static final String COMPARE_URL = "https://api-us.faceplusplus.com/facepp/v3/compare";
    private static final int REQUEST_CAMERA = 1001;
    private PreviewView previewView;
    private FrameLayout loadingOverlay;
    private TextView tvText, loadingText;
    private ImageButton btnBack;
    private ImageCapture imageCapture;
    private int countdown = 10;
    private OkHttpClient httpClient = new OkHttpClient();
    private ExecutorService bgExecutor = Executors.newSingleThreadExecutor();
    private FirebaseFirestore db;
    private String userId = UserSession.getUid();
    private String accountId, bankId, email, senderName, senderAcc, recipientName, recipientAcc, content, currentBase64;
    private int amount;
    private ProcessCameraProvider cameraProvider;
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
        setContentView(R.layout.activity_face_auth);

        previewView = findViewById(R.id.previewView);
        tvText = findViewById(R.id.tvText);
        btnBack = findViewById(R.id.btnBack);
        loadingOverlay = findViewById(R.id.loadingOverlay);
        loadingText = findViewById(R.id.loadingText);


        btnBack.setOnClickListener(v -> {
            finish();
        });

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
        type = intent.getStringExtra("type");
        billId = intent.getStringExtra("billId");
        providerCode = intent.getStringExtra("providerCode");
        customerCode = intent.getStringExtra("customerCode");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        paymentAccountId = intent.getStringExtra("paymentAccountId");
        mortgageAccountId = intent.getStringExtra("mortgageAccountId");
        savingAccountId = intent.getStringExtra("savingAccountId");



        if (checkCameraPermission()) {
            startCamera();
            startCountdown();
        }
    }

    // permissions
    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
                startCountdown();
            } else {
                Toast.makeText(this, "Cần quyền Camera để xác thực", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview,
                        imageCapture);

                startCountdown();
            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "startCamera error", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void startCountdown() {
        tvText.setVisibility(View.VISIBLE);
        countdown = 10;
        tvText.setText(String.valueOf(countdown));
        tvText.postDelayed(new Runnable() {
            @Override
            public void run() {
                countdown--;
                if (countdown <= 0) {
                    tvText.setVisibility(View.GONE);
                    takePhoto();
                } else {
                    tvText.setText(String.valueOf(countdown));
                    tvText.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera chưa sẵn sàng!", Toast.LENGTH_SHORT).show();
            return;
        }

        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        try {
                            Bitmap bmp = imageProxyToBitmap(image);
                            stopCamera();
                            image.close();
                            showLoading("Đang xác thực khuôn mặt...");

                            // face check
                            currentBase64 = bitmapToBase64(bmp);
                            fetchSavedAndCompare(currentBase64);

                        } catch (Exception e) {
                            Log.e(TAG, "capture error", e);
                            image.close();
                        }
                    }

                });
    }

    private void stopCamera() {
        if (cameraProvider != null) cameraProvider.unbindAll();
    }

    // Convert ImageProxy to Bitmap
    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP); // NO_WRAP phù hợp gửi lên API
    }

    private void fetchSavedAndCompare(String currentBase64) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(docSnapshot -> {
                    if (docSnapshot == null || !docSnapshot.exists()) {
                        Toast.makeText(this, "Không tìm thấy dữ liệu người dùng.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String savedBase64 = docSnapshot.getString("ekycFace");
                    if (savedBase64 == null || savedBase64.isEmpty()) {
                        Toast.makeText(this, "Người dùng chưa đăng ký eKYC.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Run compare
                    runCompare(savedBase64, currentBase64);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Firestore fetch error", e);
                    Toast.makeText(this, "Lấy dữ liệu lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    // Compare (Face++ compare)
    private void runCompare(String base64A, String base64B) {

        bgExecutor.execute(() -> {
            try {
                // Face++ compare accepts image_base64_1 and image_base64_2
                FormBody form = new FormBody.Builder()
                        .add("api_key", API_KEY)
                        .add("api_secret", API_SECRET)
                        .add("image_base64_1", base64A)
                        .add("image_base64_2", base64B)
                        .build();

                Request req = new Request.Builder()
                        .url(COMPARE_URL)
                        .post(form)
                        .build();

                Response resp = httpClient.newCall(req).execute();
                if (!resp.isSuccessful()) {
                    Log.e(TAG, "Compare API error code: " + resp.code());
                    runOnUiThread(() -> Toast.makeText(this,
                            "Compare API lỗi: " + resp.code(), Toast.LENGTH_LONG).show());
                    return;
                }

                String body = resp.body() != null ? resp.body().string() : "";
                Log.d(TAG, "Compare resp: " + body);

                JSONObject json = new JSONObject(body);
                double confidence = json.optDouble("confidence", -1);

                // Try get threshold 1e-5 if available
                double threshold = 70.0;
                if (json.has("thresholds")) {
                    JSONObject th = json.getJSONObject("thresholds");
                    if (th.has("1e-5")) threshold = th.optDouble("1e-5", threshold);
                }

                final boolean match = confidence >= threshold;

                runOnUiThread(() -> {
                    if (match) {
                        Toast.makeText(FaceAuthActivity.this,"Xác thực thành công!", Toast.LENGTH_LONG).show();
                        switch (type) {

                            case "TRANSFER_MONEY":
                                processTransaction();
                                break;

                            case "MORTGAGE_TOPUP":
                                processMortgageTopUp();
                                break;

                            case "MORTGAGE_WITHDRAW":
                                processMortgageWithdraw();
                                break;

                            case "SAVING_TOPUP":
                                processSavingTopUp();
                                break;

                            case "SAVING_WITHDRAW":
                                processSavingWithdraw();
                                break;

                            case "ELECTRICITY":
                                processElectricity();
                                break;

                            case "WATER":
                                processWater();
                                break;

                            default:
                                Toast.makeText(this, "Loại giao dịch không hợp lệ", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(FaceAuthActivity.this,"Xác thực thất bại.", Toast.LENGTH_LONG).show();
                        hideLoading();
                        startCamera();
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Compare exception", e);
                runOnUiThread(() -> {
                    hideLoading();
                    Toast.makeText(this,"Lỗi compare: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    // cleanup executor
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bgExecutor.shutdownNow();
    }

    private void showLoading(String message) {
        loadingText.setText(message);
        loadingOverlay.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        loadingOverlay.setVisibility(View.GONE);
    }


    /** Tìm accountId của người nhận theo số tài khoản */
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
