package com.example.project.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Notification;
import com.example.project.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> list = new ArrayList<>();
    private List<Notification> listFull = new ArrayList<>();

    public NotificationAdapter(List<Notification> list) {
        setData(list);
    }

    public void setData(List<Notification> newList) {
        this.list.clear();
        this.list.addAll(newList);

        this.listFull.clear();
        this.listFull.addAll(newList);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull NotificationAdapter.ViewHolder holder,
            int position
    ) {
        Notification noti = list.get(position);

        holder.tvDescription.setText(noti.getMessage());

        // timestamp là long
        SimpleDateFormat sdf =
                new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        String date = sdf.format(new Date(noti.getTimestamp()));

        holder.tvDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvDate, tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
