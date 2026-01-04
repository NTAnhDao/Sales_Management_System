
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
import com.example.project.Model.Electricity;
import com.example.project.Model.Water;
import com.example.project.R;
import com.example.project.Model.Bank;

import java.util.List;

public class WaterSupplierAdapter extends ArrayAdapter<Water> {

    private Context context;
    private List<Water> WaterSupplierList;

    public WaterSupplierAdapter(Context context, List<Water> WaterSupplierList) {
        super(context, 0, WaterSupplierList);
        this.context = context;
        this.WaterSupplierList = WaterSupplierList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_bank, parent, false);
        }

        Water water = WaterSupplierList.get(position);

        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);
        TextView txtShort = convertView.findViewById(R.id.txtShortName);
        TextView txtFull = convertView.findViewById(R.id.txtFullName);

        txtShort.setText(water.getCode());
        txtFull.setText(water.getName());

        Glide.with(context).load(water.getLogoUrl()).into(imgLogo);

        return convertView;
    }
}


