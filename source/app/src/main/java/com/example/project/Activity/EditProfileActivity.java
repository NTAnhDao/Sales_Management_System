package com.example.project.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.*;
import java.io.*;
import java.util.*;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.*;

public class EditProfileActivity extends AppCompatActivity {
    private TextView tvAccName;
    private EditText edtName, edtPhone, edtEmail;
    private ImageView imgAvatar, icCamera, btnBack;
    private Button btnUpdate;
    private FirebaseFirestore db;
    private String userId;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private Uri selectedImageUri;
    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                        imgAvatar.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Không thể tải ảnh!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    imgAvatar.setImageBitmap(bitmap);
                }
            }
    );
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tvAccName = findViewById(R.id.tvAccName);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);

        imgAvatar = findViewById(R.id.imgAvatar);
        icCamera = findViewById(R.id.icCamera);

        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        if (userId == null) {
            userId = UserSession.getUid();
        }

        loadUserData();

        btnBack.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        btnUpdate.setOnClickListener(v -> {
            updateProfile();
        });

        icCamera.setOnClickListener(v -> showImagePickerOptions());
    }

    private void loadUserData() {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(document -> {

                    if (document.exists()) {
                        bindUserData(document);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show()
                );
    }

    private void bindUserData(DocumentSnapshot doc) {
        String name = doc.getString("name");
        String phone = doc.getString("phone");
        String email = doc.getString("email");
        String avatarBase64 = doc.getString("avatar");

        // Load avatar nếu có
        if (avatarBase64 != null && !avatarBase64.isEmpty()) {
            byte[] imageBytes = Base64.decode(avatarBase64, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            imgAvatar.setImageBitmap(bitmap);
        } else {
            imgAvatar.setImageResource(R.drawable.img_avatar); // ảnh mặc định
        }

        // Gán dữ liệu
        tvAccName.setText(name);
        edtName.setText(name);
        edtPhone.setText(phone);
        edtEmail.setText(email);

    }

    private void showImagePickerOptions() {
        // Hiển thị tùy chọn: chọn từ Thư viện ảnh hoặc Camera
        String[] options = {"Chọn từ Thư viện ảnh", "Chụp ảnh"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Cập nhật avatar")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        pickFromGallery();
                    } else {
                        takePhoto();
                    }
                })
                .show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void takePhoto() {
        // Kiểm tra permission camera
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền Camera để chụp ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateProfile() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
        }

        String avatarBase64 = null;

        // Chuyển ảnh trong ImageView sang Base64
        if (imgAvatar.getDrawable() != null) {
            Bitmap bitmap = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos); // nén ảnh
            byte[] imageBytes = baos.toByteArray();
            avatarBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", name);
        userUpdates.put("phone", phone);
        userUpdates.put("email", email);
        if (avatarBase64 != null) {
            userUpdates.put("avatar", avatarBase64);
        }

        db.collection("users")
                .document(userId)
                .set(userUpdates, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    tvAccName.setText(name);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}
