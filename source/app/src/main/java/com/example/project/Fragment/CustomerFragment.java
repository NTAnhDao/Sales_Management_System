package com.example.project.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Activity.AddCustomerActivity;
import com.example.project.Activity.ProfileActivity;
import com.example.project.Activity.ViewProfileStaffActivity;
import com.example.project.Model.Customer;
import com.example.project.R;
import com.example.project.Adapter.CustomerAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.*;

public class CustomerFragment extends Fragment {
    private RecyclerView recyclerUser;
    private EditText etSearch;
    private ImageView btnAdd;

    private CustomerAdapter adapter;
    private List<Customer> customerList;

    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_manage_customer, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerUser = view.findViewById(R.id.recyclerUser);
        etSearch = view.findViewById(R.id.etSearch);
        btnAdd = view.findViewById(R.id.btnAdd);

        customerList = new ArrayList<>();
        adapter = new CustomerAdapter(getContext(), customerList, new CustomerAdapter.OnUserActionListener() {
            @Override
            public void onEdit(Customer customer) {
                Intent intent = new Intent(getContext(), ViewProfileStaffActivity.class);
                intent.putExtra("userId", customer.getId());
                launcher.launch(intent);
            }

            @Override
            public void onDelete(Customer customer) {
                showDeleteCustomerDialog(customer.getId(), customer.getName());
            }
        });

        recyclerUser.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerUser.setAdapter(adapter);

        loadUsers();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launcher.launch(new Intent(getContext(), AddCustomerActivity.class));
            }
        });
        return view;
    }

    private void loadUsers() {
        db.collection("users")
                .whereEqualTo("role", "customer")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String name = doc.getString("name");
                        String phone = doc.getString("phone");

                        Customer customer = new Customer(doc.getId(), name, phone, "");
                        customerList.add(customer);
                    }
                    adapter.updateFullList(customerList);
                    adapter.notifyDataSetChanged();
                });

    }

    private void deleteCustomer(String id) {
        db.collection("users").document(id)
                .collection("accounts")
                .get()
                .addOnSuccessListener(query -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit().addOnSuccessListener(aVoid -> {
                        // Xóa document user
                        db.collection("users").document(id)
                                .delete()
                                .addOnSuccessListener(aVoid1 ->
                                        Toast.makeText(getContext(), "Xóa khách hàng thành công", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Lỗi xóa khách hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    });
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Lỗi tải danh sách tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void showDeleteCustomerDialog(String userId, String userName) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa khách hàng " + userName + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    deleteCustomer(userId);
                })
                .setNegativeButton("Hủy", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // reload dữ liệu
                    loadUsers();
                }
            });


}
