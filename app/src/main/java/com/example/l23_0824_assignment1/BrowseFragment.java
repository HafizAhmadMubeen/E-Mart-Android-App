package com.example.l23_0824_assignment1;

import static android.content.Context.MODE_PRIVATE;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFragment extends Fragment {
    private ArrayList<Product> allProducts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrowseFragment newInstance(String param1, String param2) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allProducts = new ArrayList<>();
        allProducts = Product_Repository.getAllProducts();

        ImageView ivBack = view.findViewById(R.id.ivBack);
        TextView tvClearAll = view.findViewById(R.id.tvClearAll);
        EditText etSearch = view.findViewById(R.id.etSearch);


        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = etSearch.getText().toString().trim();

            if (!query.isEmpty()) {
                hideKeyboard();

                performSearch(query);

                saveSearchToHistory(query);
                saveSearchToHistory(query);
                loadSearchHistory();

                etSearch.setText("");
            }
            return true;
        });

        ivBack.setOnClickListener(v -> {
            hideKeyboard();
        });



        loadSearchHistory();
        tvClearAll.setOnClickListener(v -> {
            SharedPreferences sp = getContext().getSharedPreferences("SearchHistory", MODE_PRIVATE);
            sp.edit().remove("recent_searches_key").apply();
            hideKeyboard();
            loadSearchHistory();
        });

    }

    private void loadSearchHistory() {
        SharedPreferences sp = getContext().getSharedPreferences("SearchHistory", MODE_PRIVATE);


        Set<String> set = sp.getStringSet("recent_searches_key", new HashSet<>());

        // 2. Convert to ArrayList
        ArrayList<String> historyList = new ArrayList<>(set);

        // 3. Set Adapter
        BrowseHistoryAdapter adapter = new BrowseHistoryAdapter(getContext(), historyList);
        RecyclerView rvSearchHistory = getView().findViewById(R.id.rvSearchHistory);
        if (rvSearchHistory != null)
        {
            rvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSearchHistory.setAdapter(adapter);
        }
    }

    private void hideKeyboard() {

        View view = requireActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void performSearch(String query) {
        boolean isFound = false;

        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            new androidx.appcompat.app.AlertDialog.Builder(getContext())
                    .setMessage("Product Found.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            Toast.makeText(getContext(), "Product Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSearchToHistory(String query) {
        SharedPreferences sp = getContext().getSharedPreferences("SearchHistory", MODE_PRIVATE);

        // 1. Get the existing history (or empty set if none)
        Set<String> set = sp.getStringSet("recent_searches_key", new HashSet<>());

        // 2. Create a NEW copy of the set to ensure Android saves it
        Set<String> updatedSet = new HashSet<>(set);
        updatedSet.add(query);

        // 3. Save the updated set back to SharedPreferences
        sp.edit().putStringSet("recent_searches_key", updatedSet).apply();
    }
}