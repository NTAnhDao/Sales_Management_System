package com.example.project.utils;


import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class gửi OTP email cho user.
 * Lấy email từ Firestore theo accountNumber hoặc userId.
 */
public class EmailSender {

    private static final String TAG = "EmailSender";

    // Gmail gửi đi
    private static final String FROM_EMAIL = "anhdaoo1410@gmail.com";
    private static final String FROM_PASSWORD = "elpk nddp qzew jahb"; // App Password

    public static String generateOTP() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    /**
     * Gửi OTP cho user dựa trên accountNumber
     * @param accountNumber số tài khoản của người nhận
     */
    public static void sendOtpByAccount(String accountNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tìm user có account này
        db.collection("users")
                .get()
                .addOnSuccessListener(usersQuery -> {
                    for (DocumentSnapshot userDoc : usersQuery.getDocuments()) {
                        db.collection("users").document(userDoc.getId())
                                .collection("accounts")
                                .whereEqualTo("accountNumber", accountNumber)
                                .limit(1)
                                .get()
                                .addOnSuccessListener(accQuery -> {
                                    if (!accQuery.isEmpty()) {
                                        DocumentSnapshot accDoc = accQuery.getDocuments().get(0);
                                        String email = userDoc.getString("email"); // Lấy email user
                                        if (email != null && !email.isEmpty()) {
                                            String otp = generateOTP();
                                            sendEmail(email, otp);
                                        } else {
                                            Log.e(TAG, "User không có email!");
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Lỗi lấy user: " + e.getMessage()));
    }

    /** Gửi email OTP */
    public static void sendEmail(String toEmail, String otp) {
        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                    }
                });

                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM_EMAIL));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("DNSBank - Mã OTP Xác Thực");
                message.setText("Xin chào quý khách!\nMã OTP của bạn là: " + otp + "\nMã này chỉ có hiệu lực trong 5 phút và tuyệt đối không chia sẻ cho bất kỳ ai\nNếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.\n Cảm ơn bạn đã sử dụng DNSBank!");

                Transport.send(message);
                Log.d(TAG, "OTP gửi tới: " + toEmail + " OTP=" + otp);
            } catch (Exception e) {
                Log.e(TAG, "Lỗi gửi email: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
