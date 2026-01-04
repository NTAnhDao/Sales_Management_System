package com.example.project.Adapter;

import android.graphics.Color;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Customer;
import com.example.project.Model.Transaction;

import java.text.SimpleDateFormat;
import java.util.*;
import com.example.project.R;
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> list;

    private List<Transaction> listFull;

    public TransactionAdapter(List<Transaction> list) {
        this.list = list;
        this.listFull = new ArrayList<>(list);
    }

    public void setData(List<Transaction> newList) {
        this.list = newList;
        this.listFull = new ArrayList<>(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction t = list.get(position);

        holder.tvDescription.setText(t.getDescription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
        String date = sdf.format(t.getTimestamp());

        holder.tvDate.setText(date);

        String amountStr = (t.getAmount() > 0 ? "+" : "") + String.format("%,.0f", t.getAmount());
        holder.tvAmount.setText(amountStr);

        if (t.getAmount() > 0)
            holder.tvAmount.setTextColor(Color.GREEN);
        else
            holder.tvAmount.setTextColor(Color.RED);
    }

    @Override
    public int getItemCount() { return list.size(); }

    public void filter(String text) {
        list.clear();
        if (text.isEmpty()) {
            list.addAll(listFull);
        } else {
            String query = text.toLowerCase().trim();
            for (Transaction tr : listFull) {
                if (tr.getDescription().toLowerCase().contains(query)) {
                    list.add(tr);
                }
            }
        }
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvDate, tvAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}
