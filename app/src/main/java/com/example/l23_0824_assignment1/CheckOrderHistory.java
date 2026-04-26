package com.example.l23_0824_assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckOrderHistory extends Fragment {

    private RecyclerView rvOrderHistory;
    private OrderHistoryAdapter adapter;
    private ArrayList<OrderModel> orderList;
    private DatabaseReference ordersRef;
    private FirebaseAuth mAuth;

    public CheckOrderHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_order_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getUid();
        ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        rvOrderHistory = view.findViewById(R.id.rvOrderHistory);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(getContext(), orderList);
        rvOrderHistory.setAdapter(adapter);

        if (currentUid != null) {
            fetchOrderHistory(currentUid);
        }

        return view;
    }

    private void fetchOrderHistory(String uid) {

        ordersRef.orderByChild("sellerId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        orderList.clear();
                        if (snapshot.exists()) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                OrderModel order = ds.getValue(OrderModel.class);
                                orderList.add(0, order);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}