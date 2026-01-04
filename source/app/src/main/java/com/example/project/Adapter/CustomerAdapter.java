package com.example.project.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Model.Customer;
import com.example.project.R;

import java.util.*;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.UserViewHolder> {

    private List<Customer> customerList;
    private List<Customer> customerListFull;
    private Context context;

    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onEdit(Customer customer);
        void onDelete(Customer customer);
    }

    public CustomerAdapter(Context context, List<Customer> customerList, OnUserActionListener listener) {
        this.context = context;
        this.customerList = customerList;
        this.customerListFull = new ArrayList<>(customerList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Customer customer = customerList.get(position);

        holder.tvName.setText(customer.getName());
        holder.tvPhone.setText(customer.getPhone());
        holder.imgAvatar.setImageResource(R.drawable.img_avatar);

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(customer));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(customer));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public void filter(String text) {
        List<Customer> filteredList = new ArrayList<>();

        if (text == null || text.trim().isEmpty()) {
            // Nếu rỗng, trả lại toàn bộ danh sách gốc
            filteredList.addAll(customerListFull);
        } else {
            String query = text.toLowerCase().trim();

            for (Customer customer : customerListFull) {
                boolean matchName = customer.getName() != null &&
                        customer.getName().toLowerCase().contains(query);

                boolean matchPhone = customer.getPhone() != null &&
                        customer.getPhone().contains(query);

                if (matchName || matchPhone) {
                    filteredList.add(customer);
                }
            }
        }

        // Cập nhật danh sách hiển thị
        customerList.clear();
        customerList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void updateFullList(List<Customer> newList) {
        customerListFull.clear();
        customerListFull.addAll(newList);
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, btnEdit, btnDelete;
        TextView tvName, tvPhone;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

