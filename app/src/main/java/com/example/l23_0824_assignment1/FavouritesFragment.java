package com.example.l23_0824_assignment1;

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

public class FavouritesFragment extends Fragment {

    private RecyclerView rvFavourites;
    private Favourties_Adapter adapter;
    private ArrayList<Product> favList;

    public FavouritesFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavourites = view.findViewById(R.id.rvFavourites);
        rvFavourites.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavourites();
    }

    private void loadFavourites() {
        FavDBManager manager = new FavDBManager(getContext());
        manager.open();
        favList = manager.getAllFavourites();
        manager.close();

        if (adapter == null) {
            adapter = new Favourties_Adapter(getContext(), favList);
            rvFavourites.setAdapter(adapter);
        } else {
            adapter.updateList(favList);
        }
    }
}