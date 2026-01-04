package com.example.project.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;

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

import com.example.project.R;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class eKYCActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 101;
    private TextView timerText;
    private ImageButton btnBack;
    private PreviewView previewView;
    private View scanLine;
    private Button btnSave, btnReTakePhoto;
    private ImageCapture imageCapture;
    private Bitmap capturedBitmap;
    private FirebaseFirestore db;
    private String userId;
    private ProcessCameraProvider cameraProvider;
    private Animation animation;
    private int countdown = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekyc);

        previewView = findViewById(R.id.previewView);
        scanLine = findViewById(R.id.scanLine);
        btnSave = findViewById(R.id.btnSave);
        timerText = findViewById(R.id.timerText);
        btnBack = findViewById(R.id.btnBack);
        btnReTakePhoto = findViewById(R.id.btnReTakePhoto);

        FrameLayout frame = findViewById(R.id.faceFrame);

        animation = AnimationUtils.loadAnimation(this, R.anim.scan_anim);
        scanLine.startAnimation(animation);

        userId = getIntent().getStringExtra("userId");
        db = FirebaseFirestore.getInstance();

        checkEkycStatus();

        // kiểm tra quyền camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            startCamera();
        }

        btnReTakePhoto.setOnClickListener(v -> startCamera());
        btnSave.setOnClickListener(v -> saveEkycToFirestore());
        btnBack.setOnClickListener(v -> {
            stopCamera();
            finish();
        });
    }

    private void checkEkycStatus() {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean isVerified = Boolean.TRUE.equals(documentSnapshot.getBoolean("ekycVerified"));
                        if (!isVerified) {
                            btnReTakePhoto.setVisibility(View.GONE);
                        }
                    } 
                });
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);
        scanLine.startAnimation(animation);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setFlashMode(ImageCapture.FLASH_MODE_OFF)
                        .build();

                CameraSelector cameraSelector =
                        new CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                                .build();

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(
                        eKYCActivity.this,
                        cameraSelector,
                        preview,
                        imageCapture
                );

                startCountdown();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void startCountdown() {
        timerText.setVisibility(View.VISIBLE);
        countdown = 10;
        timerText.setText(String.valueOf(countdown));
        timerText.postDelayed(new Runnable() {
            @Override
            public void run() {
                countdown--;
                if (countdown <= 0) {
                    timerText.setVisibility(View.GONE);
                    takePhoto();
                } else {
                    timerText.setText(String.valueOf(countdown));
                    timerText.postDelayed(this, 1000);
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
                        capturedBitmap = imageProxyToBitmap(image);
                        stopCamera();
                        Toast.makeText(eKYCActivity.this, "Đã chụp ảnh!", Toast.LENGTH_SHORT).show();
                        image.close();
                        btnReTakePhoto.setVisibility(View.VISIBLE);
                    }

                });
    }

    private void stopCamera() {
        if (cameraProvider != null) cameraProvider.unbindAll();
        scanLine.clearAnimation();
        scanLine.setVisibility(View.GONE);
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private void saveEkycToFirestore() {
        if (capturedBitmap == null) {
            Toast.makeText(this, "Bạn chưa chụp ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64 = bitmapToBase64(capturedBitmap);

        Map<String, Object> data = new HashMap<>();
        data.put("ekycFace", base64);
        data.put("ekycVerified", true);

        db.collection("users").document(userId)
                .update(data)
                .addOnSuccessListener(unused ->
                        Toast.makeText(this, "Lưu eKYC thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi lưu: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Bạn phải cấp quyền CAMERA!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
