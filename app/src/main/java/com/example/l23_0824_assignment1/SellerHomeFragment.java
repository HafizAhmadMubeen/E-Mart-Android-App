package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerHomeFragment extends Fragment {

    private TextView tvWelcome;
    private FloatingActionButton fabAdd;

    private RecyclerView rvProducts;
    private SellerProductAdapter adapter;
    private ArrayList<SellerProductModelclass> list;

    public SellerHomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);

        tvWelcome = view.findViewById(R.id.tvWelcomeName);
        fabAdd = view.findViewById(R.id.fabAddProduct);
        rvProducts = view.findViewById(R.id.rvProducts);

        // Fetch user name for the "Hello Name" text
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("fullName").getValue(String.class);
                        tvWelcome.setText("Hello " + name);
                    }
                }
                @Override public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        // FAB Click to move to ProductAddActivity
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProductAddActivity.class);
            startActivity(intent);
        });

        rvProducts.setLayoutManager(new androidx.recyclerview.widget.GridLayoutManager(getActivity(), 2));

        list = new ArrayList<>();
        adapter = new SellerProductAdapter(getActivity(), list);
        rvProducts.setAdapter(adapter);

        fetchSellerProducts();

        return view;
    }
    private void fetchSellerProducts() {
        String currentUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Products");

        // Filter: Only show products where sellerId matches the logged-in user
        dbRef.orderByChild("sellerId").equalTo(currentUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SellerProductModelclass product = ds.getValue(SellerProductModelclass.class);
                            list.add(product);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
    }
}