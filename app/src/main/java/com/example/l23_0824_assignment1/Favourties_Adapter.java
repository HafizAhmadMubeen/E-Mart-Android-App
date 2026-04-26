package com.example.l23_0824_assignment1;

import android.content.Context;
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

public class Favourties_Adapter extends RecyclerView.Adapter<Favourties_Adapter.FavourtiesViewHolder> {

    Context context;
    ArrayList<Product> favList;

    public Favourties_Adapter(Context context, ArrayList<Product> favList) {
        this.context = context;
        this.favList = favList;
    }


    public void updateList(ArrayList<Product> newList) {
        this.favList.clear();
        this.favList.addAll(newList);
        notifyDataSetChanged();
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
        holder.tvPrice.setText(product.getPrice());
        holder.tvName.setText(product.getName());
        holder.tvShortDesc.setText(product.getDescription());

        holder.ivCart.setOnClickListener(v -> {
            CartDBManager db = new CartDBManager(context);
            db.open();
            db.addToCart(product);
            db.close();
            Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
        });

        holder.ivMore.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Product")
                    .setMessage("Do you want to delete this product from favourites?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FavDBManager manager = new FavDBManager(context);
                        manager.open();
                        int count = manager.deleteFavourite(product.getId());
                        manager.close();

                        if (count > 0) {
                            favList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, favList.size());
                            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
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