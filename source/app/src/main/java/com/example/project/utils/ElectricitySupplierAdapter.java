
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
import com.example.project.R;
import com.example.project.Model.Bank;

import java.util.List;

public class ElectricitySupplierAdapter extends ArrayAdapter<Electricity> {

    private Context context;
    private List<Electricity> ElectricitySupplierList;

    public ElectricitySupplierAdapter(Context context, List<Electricity> ElectricitySupplierList) {
        super(context, 0, ElectricitySupplierList);
        this.context = context;
        this.ElectricitySupplierList = ElectricitySupplierList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_bank, parent, false);
        }

        Electricity elec = ElectricitySupplierList.get(position);

        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);
        TextView txtShort = convertView.findViewById(R.id.txtShortName);
        TextView txtFull = convertView.findViewById(R.id.txtFullName);

        txtShort.setText(elec.getCode());
        txtFull.setText(elec.getName());

        Glide.with(context).load(elec.getLogoUrl()).into(imgLogo);

        return convertView;
    }
}


