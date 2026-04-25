package com.example.l23_0824_assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.SellerViewHolder> {

    Context context;
    ArrayList<SellerProductModelclass> productList;

    public SellerProductAdapter(Context context, ArrayList<SellerProductModelclass> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seller_product, parent, false);
        return new SellerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerViewHolder holder, int position) {
        SellerProductModelclass product = productList.get(position);

        holder.tvPrice.setText(product.getProductPrice());
        holder.tvName.setText(product.getProductName());
        holder.tvDesc.setText(product.getProductDescription());

        // You can add a click listener here later for the Description Activity
        holder.itemView.setOnClickListener(v -> {
            // Intent to Description Activity
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class SellerViewHolder extends RecyclerView.ViewHolder {
        TextView tvPrice, tvName, tvDesc;

        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvDesc = itemView.findViewById(R.id.tvProductDesc);
        }
    }
}