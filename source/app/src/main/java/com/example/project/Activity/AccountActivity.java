package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project.Adapter.AccountPagerAdapter;
import com.example.project.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.*;

public class AccountActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private AccountPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_customer);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        List<String> tabs = Arrays.asList("Thanh toán", "Tiết kiệm", "Vay");

        adapter = new AccountPagerAdapter(
                this,
                tabs,
                acc -> {
                    Intent i = new Intent(this, DetailRouterActivity.class);
                    i.putExtra("type", acc.getType());
                    i.putExtra("accountId", acc.getId());
                    startActivity(i);
                }
        );

        viewPager.setAdapter(adapter);

        // Liên kết tab với viewpager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabs.get(position))
        ).attach();
    }
}