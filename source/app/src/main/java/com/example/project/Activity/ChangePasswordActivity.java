package com.example.project.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputEditText edtPasswordOld, edtPasswordNew, edtRePassword;
    private Button btnUpdate;
    private ImageView btnBack;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mAuth = FirebaseAuth.getInstance();

        edtPasswordOld = findViewById(R.id.edtPasswordOld);
        edtPasswordNew = findViewById(R.id.edtPasswordNew);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnUpdate.setOnClickListener(v -> updatePassword());
    }

    private void updatePassword() {
        String oldPass = edtPasswordOld.getText().toString().trim();
        String newPass = edtPasswordNew.getText().toString().trim();
        String rePass = edtRePassword.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || rePass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(rePass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy email của người dùng để xác thực lại
        String email = user.getEmail();
        if (email == null) {
            Toast.makeText(this, "Lỗi tài khoản không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);

        // Thực hiện xác thực
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    // Sau khi xác thực OK thì cập nhật mật khẩu mới
                    user.updatePassword(newPass)
                            .addOnSuccessListener(unused ->
                                    Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show()
                            )
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Lỗi cập nhật: " + e.getMessage(), Toast.LENGTH_LONG).show()
                            );
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                );
    }
}
