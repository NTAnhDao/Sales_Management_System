package com.example.project.Activity;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.AccountAdapter;
import com.example.project.Model.Account;
import com.example.project.R;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class ViewProfileStaffActivity extends AppCompatActivity {
    private TextView tvAccName, tvName, tvPhone, tvEmail;
    private ImageView imgAvatar, icEdit, btnBack, btnAddAcc;
    private RecyclerView rcvAccountList;
    private LinearLayout containerBiometric;
    private FirebaseFirestore db;
    private String userId;
    private ArrayList<Account> accountsData;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_staff);

        tvAccName = findViewById(R.id.tvAccName);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);

        imgAvatar = findViewById(R.id.imgAvatar);
        icEdit = findViewById(R.id.icEdit);
        btnBack = findViewById(R.id.btnBack);
        btnAddAcc = findViewById(R.id.btnAddAcc);

        containerBiometric = findViewById(R.id.container_biometric);

        rcvAccountList = findViewById(R.id.rcvAccountList);
        rcvAccountList.setLayoutManager(new LinearLayoutManager(this));

        // Firestore
        db = FirebaseFirestore.getInstance();

        intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("userId");
        } else {
            Toast.makeText(this, "Không thể tải dữ liệu!", Toast.LENGTH_SHORT).show();
            finish();
        }


        loadUserData();
        handleClicks();
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
            imgAvatar.setImageResource(R.drawable.img_avatar);
        }
        

        // Gán dữ liệu
        tvAccName.setText(name);
        tvName.setText(name);
        tvPhone.setText(phone);
        tvEmail.setText(email);

        // Tải danh sách tài khoản từ subcollection
        loadAccounts();
    }

    private void loadAccounts() {
        accountsData = new ArrayList<>();
        AccountAdapter adapter = new AccountAdapter(accountsData, null);
        rcvAccountList.setAdapter(adapter);

        db.collection("users")
                .document(userId)
                .collection("accounts")
                .get()
                .addOnSuccessListener(snap -> {
                    for (DocumentSnapshot d : snap) {
                        String id = d.getId();
                        String number = d.getString("accountNumber");
                        Double bal = d.getDouble("balance");
                        String type = d.getString("accountType");

                        accountsData.add(new Account(id, number, bal, type));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thể tải tài khoản!", Toast.LENGTH_SHORT).show();
                });

    }


    private void handleClicks() {

        btnBack.setOnClickListener(v -> {
            finish();
        });

        icEdit.setOnClickListener(v -> {
            intent = new Intent(this, EditProfileActivity.class);
            intent.putExtra("userId", userId);
            launcher.launch(intent);
        });

        btnAddAcc.setOnClickListener(v -> {
            intent = new Intent(this, AddAccountActivity.class);
            intent.putExtra("userId", userId);
            launcher.launch(intent);
        });

        containerBiometric.setOnClickListener(v -> {
            intent = new Intent(this, eKYCActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
    }
    

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Activity edit xong reload dữ liệu
                    loadUserData();
                }
            });

}

