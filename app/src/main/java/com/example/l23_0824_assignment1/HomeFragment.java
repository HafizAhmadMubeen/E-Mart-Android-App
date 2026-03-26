package com.example.l23_0824_assignment1;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadHomepage();
    }

    private void loadHomepage()
    {
        RecyclerView rvRecommended = getView().findViewById(R.id.rvRecommended);
        RecyclerView rvDeals = getView().findViewById(R.id.rvDeals);

        rvRecommended.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvDeals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ArrayList<Product> products = new ArrayList<>();

        ArrayList<Product> dealProducts = new ArrayList<>();
        dealProducts.add(new Product("1", "RØDE PodMic", "$108.20", "$199.99", "Dynamic microphone", R.drawable.mic_stand,false));
        dealProducts.add(new Product("2", "Sony XM4", "$349.99", "$399.99", "Noise Cancelling", R.drawable.blackheadphone,false));
        dealProducts.add(new Product("3", "Logitech G Pro", "$129.00", "$159.00", "Gaming Headset", R.drawable.whiteheadphone,false));

        DealsAdapter dealsAdapter = new DealsAdapter(getContext(), dealProducts);
        rvDeals.setAdapter(dealsAdapter);

        for (int i = 1; i <= 25; i++) {
            products.add(new Product(
                    "ID"+i,
                    "Sony Headphone " + i,
                    "$349.99",
                    "",
                    "Model: WH-1000XM4",
                    R.drawable.blackheadphone,
                    false
            ));
        }

        rvRecommended.setHasFixedSize(true);
        Recommended_Adapter adapter = new Recommended_Adapter(getActivity(), products);
        rvRecommended.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHomepage();
    }
}