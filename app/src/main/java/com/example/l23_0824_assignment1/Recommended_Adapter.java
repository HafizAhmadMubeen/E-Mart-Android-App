package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Recommended_Adapter extends RecyclerView.Adapter<Recommended_Adapter.RecommendedViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;
    SharedPreferences sp;

    public Recommended_Adapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_recommended, parent, false);
        return new RecommendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, int position) {
        Product product = productArrayList.get(position);

        // 1. Set Text using the Firebase-mapped fields
        holder.tvRecName.setText(product.getName());
        holder.tvRecPrice.setText(product.getPrice());

        // Use getDescription() because your Firebase key is 'productDescription'
        holder.tvRecModel.setText(product.getDescription());

        // 2. Set Placeholder Image (Firebase doesn't store local Res IDs)
        holder.ivRecProduct.setImageResource(R.drawable.onboarding_icon);

        // 3. SQLite Heart Status - Remove SharedPreferences logic here
        FavDBManager manager = new FavDBManager(context);
        manager.open();
        boolean isFav = manager.isFavourite(product.getId());
        manager.close();

        holder.ivHeartRec.setImageResource(isFav ? R.drawable.filled_heart_icon : R.drawable.empty_heart_icon);

        // 4. Detail Page Navigation
        holder.cardRecommended.setOnClickListener(v -> {
            android.util.Log.d("DEBUG_SELLER", "sellerId = " + product.getSellerId());
            SharedPreferences sp1 = v.getContext().getSharedPreferences("TransferSP", Context.MODE_PRIVATE);
            sp1.edit()
                    .putString("p_name", product.getName())
                    .putString("p_price", product.getPrice())
                    .putString("p_desc", product.getDescription())
                    .putInt("p_img", product.getImageRes())
                    .putString("p_seller_id", product.getSellerId())
                    .apply();

            Intent intent = new Intent(v.getContext(), RecommendedDetailActivity.class);
            v.getContext().startActivity(intent);
        });

        // 5. Heart Click Listener (SQLite)
        holder.ivHeartRec.setOnClickListener(v -> {
            manager.open();
            if (!manager.isFavourite(product.getId())) {
                manager.addFavourite(product);
                holder.ivHeartRec.setImageResource(R.drawable.filled_heart_icon);
                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
            } else {
                manager.deleteFavourite(product.getId());
                holder.ivHeartRec.setImageResource(R.drawable.empty_heart_icon);
                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
            }
            manager.close();

            if (context instanceof MainActivity) {
                ((MainActivity) context).updateFavouritesBadge();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRecProduct , ivHeartRec;
        TextView tvRecPrice;
        TextView tvRecName;
        TextView tvRecModel;
        CardView cardRecommended;


        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRecProduct = itemView.findViewById(R.id.ivRecProduct);
            tvRecPrice = itemView.findViewById(R.id.tvRecPrice);
            tvRecName = itemView.findViewById(R.id.tvRecName);
            tvRecModel = itemView.findViewById(R.id.tvRecModel);
            ivHeartRec = itemView.findViewById(R.id.ivHeartRec);
            cardRecommended = itemView.findViewById(R.id.cardRecommended);

        }

    }
}