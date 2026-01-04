package com.example.project.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DetailRouterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getIntent().getStringExtra("type");
        String id = getIntent().getStringExtra("accountId");

        Intent i;

        switch (type) {
            case "Thanh toán":
                i = new Intent(this, CheckingAccountActivity.class);
                break;

            case "Tiết kiệm":
                i = new Intent(this, SavingAccountActivity.class);
                break;

            case "Vay":
                i = new Intent(this, MortgageAccountActivity.class);
                break;

            default:
                i = new Intent(this, CheckingAccountActivity.class);
        }

        i.putExtra("accountId", id);
        startActivity(i);
        finish();
    }
}
