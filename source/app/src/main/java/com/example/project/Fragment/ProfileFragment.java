package com.example.project.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.*;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.project.Activity.AddAccountActivity;
import com.example.project.Activity.ChangePasswordActivity;
import com.example.project.Activity.EditProfileActivity;
import com.example.project.Activity.LoginActivity;
import com.example.project.R;
import com.example.project.Activity.UserSession;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private TextView tvName, tvPhone, tvEmail;
    private ImageView btnBack;
    private LinearLayout containerLogout;
    private FirebaseFirestore db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile_staff, container, false);

        // === ÁNH XẠ VIEW ===
        tvName = view.findViewById(R.id.tvName);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);



        containerLogout = view.findViewById(R.id.container_logout);

        // Firestore
        db = FirebaseFirestore.getInstance();

        userId = UserSession.getUid();

        loadUserData();
        handleClicks();

        return view;
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
                        Toast.makeText(getContext(), "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show()
                );
    }

    private void bindUserData(DocumentSnapshot doc) {
        String name = doc.getString("name");
        String phone = doc.getString("phone");
        String email = doc.getString("email");
        String role = doc.getString("role");

        // Gán dữ liệu
        tvName.setText(name);
        tvPhone.setText(phone);
        tvEmail.setText(email);
    }


    private void handleClicks() {
        containerLogout.setOnClickListener(v -> {
            UserSession.logout();
            userId = null;

            if (getArguments() != null) {
                getArguments().clear();
            }

            // Chuyển về màn hình Login
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

}

