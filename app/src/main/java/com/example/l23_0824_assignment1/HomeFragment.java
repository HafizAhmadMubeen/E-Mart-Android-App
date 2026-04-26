package com.example.l23_0824_assignment1;

import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.google.firebase.database.*;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView rvRecommended, rvDeals;
    private Recommended_Adapter recAdapter;
    private DealsAdapter dealsAdapter;
    private ArrayList<Product> productList;
    private DatabaseReference dbRef;

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

        rvRecommended.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvDeals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recAdapter = new Recommended_Adapter(getActivity(), productList);
        dealsAdapter = new DealsAdapter(getContext(), productList);

        rvRecommended.setAdapter(recAdapter);
        rvDeals.setAdapter(dealsAdapter);

        fetchRealtimeData();
    }

    private void fetchRealtimeData() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    if (p != null) {
                        // IMPORTANT: Always set the ID from the Firebase key
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