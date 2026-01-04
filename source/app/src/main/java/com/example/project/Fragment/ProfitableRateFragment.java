package com.example.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfitableRateFragment extends Fragment {
    private TextView txtCurrentRate;
    private EditText edtRate;
    private Button btnSave;
    private FirebaseFirestore db;
    private DocumentReference rateRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profitable_rate, container, false);

        txtCurrentRate = view.findViewById(R.id.txtCurrentRate);
        edtRate = view.findViewById(R.id.edtRate);
        btnSave = view.findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();
        rateRef = db.collection("bankSettings").document("profitRate");

        loadCurrentRate();

        btnSave.setOnClickListener(v -> saveRate());

        return view;
    }

    private void loadCurrentRate() {
        rateRef.get().addOnSuccessListener(document -> {
            if (document.exists()) {
                // Document đã tồn tại
                double rate = document.getDouble("rate");
                txtCurrentRate.setText("Lãi suất hiện tại: " + rate + "%/năm");
            } else {
                // Document chưa có thì tạo document mặc định
                createDefaultRate();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
        });
    }

    private void createDefaultRate() {
        Map<String, Object> defaultData = new HashMap<>();
        defaultData.put("rate", 0); // default value

        rateRef.set(defaultData)
                .addOnSuccessListener(unused -> {
                    txtCurrentRate.setText("Lãi suất hiện tại: --%/năm");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveRate() {
        String newRateStr = edtRate.getText().toString().trim();

        if (newRateStr.isEmpty()) {
            edtRate.setError("Vui lòng nhập lãi suất mới");
            return;
        }

        double newRate = Double.parseDouble(newRateStr);

        Map<String, Object> data = new HashMap<>();
        data.put("rate", newRate);

        rateRef.set(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Lãi suất đã được cập nhật", Toast.LENGTH_SHORT).show();
                    txtCurrentRate.setText("Lãi suất hiện tại: " + newRate + "%/năm");
                    edtRate.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Cập nhật thất bại. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show();
                });
    }
}

