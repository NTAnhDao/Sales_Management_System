package com.example.project.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.project.Model.Notification;
import com.example.project.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "BALANCE_CHANNEL";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean show = intent.getBooleanExtra("showNotification", true);
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        // 🔥 dữ liệu thêm để lưu firebase
        String accountId = intent.getStringExtra("accountId");
        String type = intent.getStringExtra("type"); // SEND / RECEIVE
        long amount = intent.getLongExtra("amount", 0);

        if (title == null || message == null) return;

        saveNotificationToFirestore(accountId, message, type, amount);

        // ================== ❌ 2. KHÔNG HIỆN NOTIFICATION ==================
        if (!show) return;

        // ================== HIỂN THỊ NOTIFICATION ==================
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Biến động số dư",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

        // Android 13+ cần quyền POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        managerCompat.notify((int) System.currentTimeMillis(), builder.build());

        // ================== LƯU FIREBASE ==================

    }

    private void saveNotificationToFirestore(String accountId, String message, String type, long amount) {

        if (accountId == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String id = db.collection("notifications").document().getId();

        Notification noti = new Notification(
                id,
                accountId,
                message,
                type,
                amount,
                System.currentTimeMillis()


        );

        db.collection("notifications")
                .document(id)
                .set(noti)
                .addOnSuccessListener(v ->
                        Log.d("NOTI_FIREBASE", "✅ Lưu thông báo thành công"))
                .addOnFailureListener(e ->
                        Log.e("NOTI_FIREBASE", "❌ Lỗi lưu thông báo", e));
    }



}
