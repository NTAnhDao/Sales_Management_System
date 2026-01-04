package com.example.project.Adapter;

import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Account;
import com.example.project.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {

    private List<Account> list;
    private AccountPagerAdapter.OnAccountClick listener;

    public AccountAdapter(List<Account> list, AccountPagerAdapter.OnAccountClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setData(List<Account> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account acc = list.get(position);

        holder.tvNumberAcc.setText(acc.getAccountNumber());
        holder.tvType.setText(acc.getType());
        holder.tvBalance.setText(String.format("%,.0f VND", acc.getBalance()));

        // Nếu listener == null, Không xử lý click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(acc);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumberAcc, tvType, tvBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNumberAcc = itemView.findViewById(R.id.tvNumberAcc);
            tvType = itemView.findViewById(R.id.tvType);
            tvBalance = itemView.findViewById(R.id.tvBalance);
        }
    }
}

