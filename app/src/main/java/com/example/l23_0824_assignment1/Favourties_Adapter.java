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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Favourties_Adapter extends RecyclerView.Adapter<Favourties_Adapter.FavourtiesViewHolder> {

    Context context;
    ArrayList<Product> favList;

    SharedPreferences CartSp, favSp;
    public Favourties_Adapter(Context context, ArrayList<Product> favList) {
        this.context = context;
        this.favList = favList;
    }



    @NonNull
    @Override
    public FavourtiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.favourties_item_design, parent, false);
        return new FavourtiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavourtiesViewHolder holder, int position) {
        Product product = favList.get(position);

        holder.ivProduct.setImageResource(product.getImageRes());
        holder.tvPrice.setText(product.getPrice());
        holder.tvName.setText(product.getName());
        holder.tvShortDesc.setText(product.getShortDescription());


        holder.ivCart.setOnClickListener(v -> {
            CartSp = context.getSharedPreferences("cartList", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = CartSp.edit();

            editor.putInt(product.getName() + "_qty", 1);
            editor.putString(product.getName() + "_price", product.getPrice());
            editor.apply();
            CartManager.addProduct(product);


            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();

        });

        holder.ivMore.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Product");
            builder.setMessage("Do you want to delete this product from favourites?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                favSp = context.getSharedPreferences("favourites", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = favSp.edit();


                Set<String> currentSet = favSp.getStringSet("fav_names_set", new HashSet<>());
                Set<String> updatedSet = new HashSet<>(currentSet);


                updatedSet.remove(product.getName());
                editor.putStringSet("fav_names_set", updatedSet);


                editor.remove(product.getName() + "_price");
                editor.remove(product.getName() + "_desc");
                editor.apply();


                favList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, favList.size());
                if (context instanceof MainActivity) {
                    ((MainActivity) context).updateFavouritesBadge();
                }

                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return favList.size();
    }

    public static class FavourtiesViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivCart, ivMore;
        TextView tvPrice, tvName, tvShortDesc;

        public FavourtiesViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivFavProduct);
            ivCart = itemView.findViewById(R.id.ivCart);
            ivMore = itemView.findViewById(R.id.ivMoreOptions);
            tvPrice = itemView.findViewById(R.id.tvFavPrice);
            tvName = itemView.findViewById(R.id.tvFavName);
            tvShortDesc = itemView.findViewById(R.id.tvFavDesc);
        }
    }


}
