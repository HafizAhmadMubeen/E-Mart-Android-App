package com.example.l23_0824_assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recommended_Adapter extends RecyclerView.Adapter<Recommended_Adapter.RecommendedViewHolder> {

    Context context;
    ArrayList<Product> productArrayList;

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
        holder.ivRecProduct.setImageResource(product.getImageRes());
        holder.tvRecPrice.setText(product.getPrice());
        holder.tvRecName.setText(product.getName());
        holder.tvRecModel.setText(product.getDescription());


    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {

        ImageView ivRecProduct;
        TextView tvRecPrice;
        TextView tvRecName;
        TextView tvRecModel;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);

            ivRecProduct = itemView.findViewById(R.id.ivRecProduct);
            tvRecPrice = itemView.findViewById(R.id.tvRecPrice);
            tvRecName = itemView.findViewById(R.id.tvRecName);
            tvRecModel = itemView.findViewById(R.id.tvRecModel);
        }

    }
}