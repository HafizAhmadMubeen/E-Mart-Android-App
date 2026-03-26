package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BrowseHistoryAdapter extends RecyclerView.Adapter<BrowseHistoryAdapter.BrowseHistoryViewHolder> {


    private Context context;
    private ArrayList<String> historyList;
    private String SearchHashKey = "recent_searches_key";

    public BrowseHistoryAdapter(Context context, ArrayList<String> historyList) {
        this.context = context;
        this.historyList = historyList;
    }


    @NonNull
    @Override
    public BrowseHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_search_history_design, parent, false);
        return new BrowseHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseHistoryViewHolder holder, int position) {
        String searchTerm = historyList.get(position);
        holder.tvSearchTerm.setText(searchTerm);

        holder.ivDelete.setOnClickListener(v -> {
            SharedPreferences sp = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE);
            Set<String> currentSet = sp.getStringSet(SearchHashKey, new HashSet<>());
            Set<String> updatedSet = new HashSet<>(currentSet);
            updatedSet.remove(searchTerm);
            sp.edit().putStringSet(SearchHashKey, updatedSet).apply();


            historyList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, historyList.size());
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class BrowseHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvSearchTerm;
        ImageView ivDelete;

        public BrowseHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSearchTerm = itemView.findViewById(R.id.tvSearchTerm);
            ivDelete = itemView.findViewById(R.id.ivDeleteSearch);
        }
    }
}