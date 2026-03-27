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
        String name = product.getName();


        holder.ivProduct.setImageResource(product.getImageRes());
        holder.tvName.setText(name);
        holder.tvNewPrice.setText(product.getPrice());
        holder.tvDesc.setText(product.getShortDescription());


        holder.tvOldPrice.setText(product.getOriginalPrice());
        holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);


        SharedPreferences sp = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);


        Set<String> favSet = sp.getStringSet("fav_names_set", new HashSet<>());


        if (favSet.contains(name)) {
            holder.ivHeart.setImageResource(R.drawable.filled_heart_icon);
        } else {
            holder.ivHeart.setImageResource(R.drawable.empty_heart_icon);
        }

        holder.cardDeal.setOnClickListener(v -> {
            SharedPreferences sp1 = v.getContext().getSharedPreferences("TransferSP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();

            // Save every piece of info individually
            editor.putString("p_name", product.getName());
            editor.putString("p_price", product.getPrice());
            editor.putString("p_desc", product.getDescription());
            editor.putInt("p_img", product.getImageRes());

            editor.apply();

            Intent intent = new Intent(v.getContext(), RecommendedDetailActivity.class);
            v.getContext().startActivity(intent);
        });



        holder.ivHeart.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sp.edit();


            Set<String> updatedSet = new HashSet<>(sp.getStringSet("fav_names_set", new HashSet<>()));

            if (!updatedSet.contains(name)) {

                updatedSet.add(name);
                editor.putString(name + "_price", product.getPrice());
                editor.putString(name + "_desc", product.getShortDescription());
                editor.putInt(name + "_img", product.getImageRes());

                holder.ivHeart.setImageResource(R.drawable.filled_heart_icon);


                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
            } else {

                updatedSet.remove(name);
                editor.remove(name + "_price");
                editor.remove(name + "_desc");
                editor.remove(name + "_img");

                holder.ivHeart.setImageResource(R.drawable.empty_heart_icon);
                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
            }


            editor.putStringSet("fav_names_set", updatedSet);
            editor.apply();

            if (context instanceof MainActivity) {
                ((MainActivity) context).updateFavouritesBadge();
            }
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