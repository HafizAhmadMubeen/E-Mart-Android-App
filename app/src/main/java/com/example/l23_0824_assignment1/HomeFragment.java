package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView rvRecommended, rvDeals;
    private Recommended_Adapter recAdapter;
    private DealsAdapter dealsAdapter;
    private ArrayList<Product> productList;
    private DatabaseReference dbRef;
    private FloatingActionButton fabChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbRef = FirebaseDatabase.getInstance().getReference("Products");
        productList = new ArrayList<>();

        rvRecommended = view.findViewById(R.id.rvRecommended);
        rvDeals = view.findViewById(R.id.rvDeals);
        fabChat = view.findViewById(R.id.fabChat);

        rvRecommended.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvDeals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recAdapter = new Recommended_Adapter(getActivity(), productList);
        dealsAdapter = new DealsAdapter(getContext(), productList);

        rvRecommended.setAdapter(recAdapter);
        rvDeals.setAdapter(dealsAdapter);

        fetchRealtimeData();

        // ✅ FAB click — chat with seller of last viewed product
        fabChat.setOnClickListener(v -> {
            SharedPreferences sp = requireContext()
                    .getSharedPreferences("TransferSP", Context.MODE_PRIVATE);
            String sellerId = sp.getString("p_seller_id", "");

            if (sellerId.isEmpty()) {
                Toast.makeText(getContext(),
                        "Please open a product first to chat with its seller",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Fetch seller name from Firebase then open chat
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(sellerId)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        String sellerName = "Seller";
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null && user.fullName != null) {
                                sellerName = user.fullName;
                            }
                        }
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("receiverId", sellerId);
                        intent.putExtra("receiverName", sellerName);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(),
                                "Could not reach seller", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void fetchRealtimeData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    if (p != null) {
                        p.setProductId(ds.getKey());
                        productList.add(p);
                    }
                }
                recAdapter.notifyDataSetChanged();
                dealsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}