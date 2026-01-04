package com.example.project.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project.Fragment.CustomerFragment;
import com.example.project.Fragment.ProfitableRateFragment;
import com.example.project.Fragment.ProfileFragment;
import com.example.project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class OfficerMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        bottomNavigationView = findViewById(R.id.bottomNav);

        // --- Load fragment mặc định ---
        if (savedInstanceState == null) {
            loadFragment(new CustomerFragment());
            bottomNavigationView.setSelectedItemId(R.id.nav_customer);
        }

        // --- Xử lý khi chọn item ---
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_profit_rate) {
                selectedFragment = new ProfitableRateFragment();
            } else if (itemId == R.id.nav_customer) {
                selectedFragment = new CustomerFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });
    }

    // Hàm đổi Fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
