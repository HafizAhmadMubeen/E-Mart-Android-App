package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    Context context;
    ArrayList<Product> dealsList;

    SharedPreferences sp;

    public DealsAdapter(Context context, ArrayList<Product> dealsList) {
        this.context = context;
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Product product = dealsList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvNewPrice.setText(product.getPrice());
        holder.tvDesc.setText(product.getDescription());
        holder.ivProduct.setImageResource(product.getImageRes());

        // Check SQLite for Heart Status
        FavDBManager favDB = new FavDBManager(context);
        favDB.open();
        boolean isFav = favDB.isFavourite(product.getId());
        favDB.close();

        holder.ivHeart.setImageResource(isFav ? R.drawable.filled_heart_icon : R.drawable.empty_heart_icon);

        holder.ivHeart.setOnClickListener(v -> {
            favDB.open();
            if (!favDB.isFavourite(product.getId())) {
                favDB.addFavourite(product);
                holder.ivHeart.setImageResource(R.drawable.filled_heart_icon);
            } else {
                favDB.deleteFavourite(product.getId());
                holder.ivHeart.setImageResource(R.drawable.empty_heart_icon);
            }
            favDB.close();
        });

        holder.cardDeal.setOnClickListener(v -> {
            SharedPreferences sp = context.getSharedPreferences("TransferSP", Context.MODE_PRIVATE);
            sp.edit().putString("p_name", product.getName())
                    .putString("p_price", product.getPrice())
                    .putString("p_desc", product.getDescription())
                    .putInt("p_img", product.getImageRes())
                    .apply();
            context.startActivity(new Intent(context, RecommendedDetailActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    public static class DealViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivHeart;
        TextView tvName, tvNewPrice, tvOldPrice, tvDesc;
        CardView cardDeal;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivDealProduct);
            ivHeart = itemView.findViewById(R.id.ivHeartDeal);
            tvName = itemView.findViewById(R.id.tvDealName);
            tvNewPrice = itemView.findViewById(R.id.tvNewPrice);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);
            tvDesc = itemView.findViewById(R.id.tvDealDesc);
            cardDeal = itemView.findViewById(R.id.cardDeal);

        }
    }
}