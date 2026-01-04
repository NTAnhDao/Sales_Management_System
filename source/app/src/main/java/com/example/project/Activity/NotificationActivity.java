package com.example.project.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Adapter.NotificationAdapter;
import com.example.project.Model.Notification;
import com.example.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rvNotification;
    private NotificationAdapter adapter;
    private List<Notification> list;
    private FirebaseFirestore db;
    private ImageView btnBack;

    private String currentAccountId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        rvNotification = findViewById(R.id.recyclerNotifications);
        btnBack = findViewById(R.id.btnBack);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new NotificationAdapter(list);
        rvNotification.setAdapter(adapter);
        btnBack.setOnClickListener(v -> finish());


        db = FirebaseFirestore.getInstance();

        // 🔑 accountId tài khoản đang đăng nhập
        currentAccountId = getIntent().getStringExtra("accountId");

        loadNotifications();
    }

    private void loadNotifications() {
        db.collection("notifications")
                .whereEqualTo("accountId", currentAccountId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    List<Notification> temp = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Notification n = doc.toObject(Notification.class);
                        if (n != null) temp.add(n);
                    }

                    adapter.setData(temp);
                });
    }
}
