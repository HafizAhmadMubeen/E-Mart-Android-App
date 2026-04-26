package com.example.l23_0824_assignment1;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavouritesFragment extends Fragment {

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvFavourites = view.findViewById(R.id.rvFavourites);
        rvFavourites.setLayoutManager(new LinearLayoutManager(getContext()));

        loadfavourties();
    }

    private void loadfavourties() {
        FavDBManager manager = new FavDBManager(getContext());
        manager.open();
        ArrayList<Product> favList = manager.getAllFavourites();
        manager.close();

        Favourties_Adapter adapter = new Favourties_Adapter(getContext(), favList);
        RecyclerView rvFavourites = getView().findViewById(R.id.rvFavourites);
        if (rvFavourites != null) {
            rvFavourites.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadfavourties();
    }
}