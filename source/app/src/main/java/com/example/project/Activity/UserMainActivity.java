package com.example.project.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.example.project.R;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserMainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private TextView tvAccountName, tvAccountNumber, tvBalance, ViewHistory, TransferMoney;
    private ImageView imgEye, imageNotification;
    private ImageButton btnDepositPhone, btnPayElectricity, btnWaterBill, btnBuyTicket, btnBookHotel, btnBuyOnl;
    private LinearLayout AccountContainer, UserContainer;
    private View clickBlocker;
    private String accountId; // ID account thanh toán

    private boolean isBalanceVisible = true;
    private String balanceRealValue = "0";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLat = 0, currentLng = 0;
    private MapView mapView;
    private GoogleMap gMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Ánh xạ
        tvAccountName = findViewById(R.id.tvAccountName);
        tvAccountNumber = findViewById(R.id.tvAccountNumber);
        tvBalance = findViewById(R.id.tvBalance);
        imgEye = findViewById(R.id.imgEye);
        imageNotification = findViewById(R.id.imgNotifications);

        ViewHistory = findViewById(R.id.ViewHistory);
        TransferMoney = findViewById(R.id.TransferMoney);

        btnDepositPhone = findViewById(R.id.btnDepositPhone);
        btnPayElectricity = findViewById(R.id.btnPayElectricity);
        btnWaterBill = findViewById(R.id.btnWaterBill);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        btnBookHotel = findViewById(R.id.btnBookHotel);
        btnBuyOnl = findViewById(R.id.btnBuyOnl);

        AccountContainer = findViewById(R.id.AccountContainer);
        UserContainer = findViewById(R.id.UserContainer);
        clickBlocker = findViewById(R.id.clickBlocker);

        mapView = findViewById(R.id.mapView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        loadCustomerData();
        checkUserPin();
        setupFunctions();

    }

    private void checkUserPin() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String pin = documentSnapshot.getString("pin");

                    // Nếu chưa tạo PIN
                    if (pin == null || pin.isEmpty()) {
                        showCreatePinDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không kiểm tra được mã PIN", Toast.LENGTH_SHORT).show();
                });
    }

    private void showCreatePinDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Thiết lập mã PIN")
                .setMessage("Bạn chưa tạo mã PIN. Vui lòng tạo mã PIN để sử dụng các chức năng thanh toán.")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    Intent intent = new Intent(UserMainActivity.this, AddPinActivity.class);
                    startActivity(intent);
                })
                .show();
    }


    private void loadCustomerData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String name = userDoc.getString("name");
                    tvAccountName.setText(name != null ? name : "USER");

                    // Lấy tài khoản thanh toán từ subcollection accounts
                    db.collection("users")
                            .document(uid)
                            .collection("accounts")
                            .whereEqualTo("accountType", "Thanh toán")
                            .limit(1)
                            .get()
                            .addOnSuccessListener(accQuery -> {
                                if (!accQuery.isEmpty()) {
                                    DocumentSnapshot accDoc = accQuery.getDocuments().get(0);
                                    accountId = accDoc.getId();
                                    String accountNumber = accDoc.getString("accountNumber");
                                    Double balance = accDoc.getDouble("balance");
                                    imageNotification.setOnClickListener(v -> {
                                        Intent i = new Intent(this, NotificationActivity.class);
                                        i.putExtra("accountId", accountId);
                                        startActivity(i);
                                    });

                                    tvAccountNumber.setText(accountNumber != null ? accountNumber : "00000000");
                                    if (balance != null) {
                                        balanceRealValue= String.format("%,.0f", balance);
                                        tvBalance.setText(balanceRealValue);
                                    }
                                } else {
                                    tvAccountNumber.setText("00000000");
                                    tvBalance.setText("0");
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Lỗi tải tài khoản", Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải dữ liệu user", Toast.LENGTH_SHORT).show();
                });
    }

    private void setupFunctions() {

        // Ẩn / hiện số dư
        imgEye.setOnClickListener(v -> {
            if (isBalanceVisible) {
                tvBalance.setText("*******");
                imgEye.setImageResource(R.drawable.ic_eye_off);
            } else {
                tvBalance.setText(balanceRealValue);
                imgEye.setImageResource(R.drawable.open_eye);
            }
            isBalanceVisible = !isBalanceVisible;
        });

        // Lịch sử giao dịch
        ViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, CheckingAccountActivity.class);
            intent.putExtra("accountId", accountId);
            startActivity(intent);
        });


        // Chuyển tiền
        TransferMoney.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, TransferMoneyActivity.class);
            intent.putExtra("accountNumber", tvAccountNumber.getText().toString());
            startActivity(intent);
        });


        // Nạp tiền điện thoại
        btnDepositPhone.setOnClickListener(v -> {
            Intent intent = new Intent(UserMainActivity.this, DepositPhoneActivity.class);
            startActivity(intent);
        });

        // Tiền điện
        btnPayElectricity.setOnClickListener(v ->
                startActivity(new Intent(this, PayElectricityActivity.class))
        );

        // Tiền nước
        btnWaterBill.setOnClickListener(v ->
                startActivity(new Intent(this, PayWaterActivity.class))
        );
        

        // Tài khoản
        AccountContainer.setOnClickListener(v ->
                startActivity(new Intent(this, AccountActivity.class))
        );

        // Cá nhân
        UserContainer.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // Mở Google Maps
        mapView.setOnTouchListener((v, e) -> true);
        clickBlocker.setOnClickListener(v ->
            startActivity(new Intent(this, MapActivity.class))
        );
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setAllGesturesEnabled(true);
        gMap.getUiSettings().setScrollGesturesEnabled(false);
        gMap.getUiSettings().setZoomGesturesEnabled(false);
        gMap.getUiSettings().setRotateGesturesEnabled(false);
        gMap.getUiSettings().setTiltGesturesEnabled(false);

        // Kiểm tra quyền
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001);
            return;
        }

        // Lấy vị trí hiện tại
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
                        gMap.clear();
                        gMap.addMarker(new MarkerOptions().position(current).title("Vị trí hiện tại"));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15f));
                    }
                });

    }

    // Xử lý lifecycle MapView
    @Override protected void onResume() {
        super.onResume();
        mapView.onResume();

        loadCustomerData();
    }
    @Override protected void onStart() { super.onStart(); mapView.onStart(); }
    @Override protected void onStop() { super.onStop(); mapView.onStop(); }
    @Override protected void onPause() { mapView.onPause(); super.onPause(); }
    @Override protected void onDestroy() { mapView.onDestroy(); super.onDestroy(); }
    @Override public void onLowMemory() { super.onLowMemory(); mapView.onLowMemory(); }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    // Xử lý quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onMapReady(gMap);
        }
    }


}