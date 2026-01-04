package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.UserSession;
import com.example.project.Model.Account;
import com.example.project.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AccountPagerAdapter extends RecyclerView.Adapter<AccountPagerAdapter.PageViewHolder> {

    private Context context;
    private List<String> tabTypes;
    private OnAccountClick onAccountClick;

    public interface OnAccountClick {
        void onClick(Account acc);
    }

    public AccountPagerAdapter(Context context, List<String> tabTypes, OnAccountClick onAccountClick) {
        this.context = context;
        this.tabTypes = tabTypes;
        this.onAccountClick = onAccountClick;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.page_account_list, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        String type = tabTypes.get(position);
        holder.loadAccounts(type);
    }

    @Override
    public int getItemCount() {
        return tabTypes.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recycler;
        AccountAdapter adapter;
        FirebaseFirestore db;
        String uid;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);

            recycler = itemView.findViewById(R.id.recyclerAccountList);
            recycler.setLayoutManager(new LinearLayoutManager(context));
            adapter = new AccountAdapter(new ArrayList<>(), onAccountClick);
            recycler.setAdapter(adapter);

            db = FirebaseFirestore.getInstance();
            uid = UserSession.getUid();
        }

        void loadAccounts(String type) {
            db.collection("users")
                    .document(uid)
                    .collection("accounts")
                    .whereEqualTo("accountType", type)
                    .get()
                    .addOnSuccessListener(qs -> {
                        List<Account> list = new ArrayList<>();
                        for (DocumentSnapshot d : qs) {
                            String id = d.getId();
                            String number = d.getString("accountNumber");
                            Double bal = d.getDouble("balance");
                            Account acc = new Account(id, number, bal, type);
                            acc.setId(d.getId());
                            list.add(acc);
                        }
                        adapter.setData(list);
                    });
        }
    }
}

