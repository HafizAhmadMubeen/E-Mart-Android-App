package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        String name = product.getName();


        holder.ivRecProduct.setImageResource(product.getImageRes());
        holder.tvRecPrice.setText(product.getPrice());
        holder.tvRecName.setText(name);
        holder.tvRecModel.setText(product.getDescription());


        SharedPreferences sp = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);


        Set<String> favSet = sp.getStringSet("fav_names_set", new HashSet<>());

        if (favSet.contains(name)) {
            holder.ivHeartRec.setImageResource(R.drawable.filled_heart_icon);
        } else {
            holder.ivHeartRec.setImageResource(R.drawable.empty_heart_icon);
        }


        holder.ivHeartRec.setOnClickListener(v -> {

            Set<String> updatedSet = new HashSet<>(sp.getStringSet("fav_names_set", new HashSet<>()));
            SharedPreferences.Editor editor = sp.edit();

            if (!updatedSet.contains(name)) {
                updatedSet.add(name);
                editor.putString(name + "_price", product.getPrice());
                editor.putString(name + "_desc", product.getDescription());
                editor.putInt(product.getName() + "_img", product.getImageRes());

                holder.ivHeartRec.setImageResource(R.drawable.filled_heart_icon);
                Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
            } else {
                updatedSet.remove(name);
                editor.remove(name + "_price");
                editor.remove(name + "_desc");
                editor.remove(product.getName() + "_img");


                holder.ivHeartRec.setImageResource(R.drawable.empty_heart_icon);
                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
            }
            editor.putStringSet("fav_names_set", updatedSet);
            editor.apply();
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

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRecProduct = itemView.findViewById(R.id.ivRecProduct);
            tvRecPrice = itemView.findViewById(R.id.tvRecPrice);
            tvRecName = itemView.findViewById(R.id.tvRecName);
            tvRecModel = itemView.findViewById(R.id.tvRecModel);
            ivHeartRec = itemView.findViewById(R.id.ivHeartRec);
        }

    }
}