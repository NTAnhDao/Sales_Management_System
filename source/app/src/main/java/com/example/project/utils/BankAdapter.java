package com.example.project.utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.project.R;
import com.example.project.Model.Bank;

import java.util.List;

public class BankAdapter extends ArrayAdapter<Bank> {

    private Context context;
    private List<Bank> bankList;

    public BankAdapter(Context context, List<Bank> bankList) {
        super(context, 0, bankList);
        this.context = context;
        this.bankList = bankList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_bank, parent, false);
        }

        Bank bank = bankList.get(position);

        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);
        TextView txtShort = convertView.findViewById(R.id.txtShortName);
        TextView txtFull = convertView.findViewById(R.id.txtFullName);

        txtShort.setText(bank.getCode());
        txtFull.setText(bank.getName());

        Glide.with(context).load(bank.getLogoUrl()).into(imgLogo);

        return convertView;
    }
}

