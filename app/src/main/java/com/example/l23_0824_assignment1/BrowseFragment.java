package com.example.l23_0824_assignment1;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BrowseFragment extends Fragment {
    private SearchHistoryDB dbHelper;
    private DatabaseReference productsRef;
    private ArrayList<Product> firebaseProducts;

    public BrowseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new SearchHistoryDB(getContext());
        firebaseProducts = new ArrayList<>();
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        // Continuously keep a local copy of products for fast searching
        listenForProducts();

        ImageView ivBack = view.findViewById(R.id.ivBack);
        TextView tvClearAll = view.findViewById(R.id.tvClearAll);
        EditText etSearch = view.findViewById(R.id.etSearch);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                hideKeyboard();
                performSearch(query);
                dbHelper.addHistory(query); // SQLite save
                loadSearchHistory();
                etSearch.setText("");
            }
            return true;
        });

        tvClearAll.setOnClickListener(v -> {
            dbHelper.clearHistory(); // SQLite clear
            loadSearchHistory();
        });

        loadSearchHistory();
    }

    private void listenForProducts() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                firebaseProducts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Product p = ds.getValue(Product.class);
                    if (p != null) firebaseProducts.add(p);
                }
            }
            @Override public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void loadSearchHistory() {
        ArrayList<String> historyList = dbHelper.getAllHistory();
        BrowseHistoryAdapter adapter = new BrowseHistoryAdapter(getContext(), historyList);
        RecyclerView rvSearchHistory = getView().findViewById(R.id.rvSearchHistory);
        if (rvSearchHistory != null) {
            rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSearchHistory.setAdapter(adapter);
        }
    }

    private void performSearch(String query) {
        boolean isFound = false;
        for (Product p : firebaseProducts) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setMessage("Product Found in E-Mart Database.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            Toast.makeText(getContext(), "No such product available", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}