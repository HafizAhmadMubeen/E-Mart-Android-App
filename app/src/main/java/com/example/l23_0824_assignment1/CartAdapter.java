package com.example.l23_0824_assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

interface OnCartChangedListener {
    void onPriceChanged(double newTotal);
}
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{


    private Context context;
    private ArrayList<CartItem> cartItems;
    private OnCartChangedListener listener;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, OnCartChangedListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_design, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        Product product = item.getProduct();

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.tvShortDesc.setText(product.getShortDescription());
        holder.ivProduct.setImageResource(product.getImageRes());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            calculateTotal();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                calculateTotal();
            } else {
                // Optional: Remove item if quantity goes to 0
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
                calculateTotal();
            }
        });

        holder.ivMenu.setOnClickListener(v -> {
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            calculateTotal();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            // Remove "$" and convert to double
            String priceStr = item.getProduct().getPrice().replace("$", "").trim();
            double price = Double.parseDouble(priceStr);
            total += (price * item.getQuantity());
        }
        listener.onPriceChanged(total);
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice, tvShortDesc, tvQuantity;
        ImageView ivProduct, ivMenu;
        ImageButton btnPlus, btnMinus;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvCartName);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            tvShortDesc = itemView.findViewById(R.id.tvCartShortDesc);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivProduct = itemView.findViewById(R.id.ivCartProduct);
            ivMenu = itemView.findViewById(R.id.ivCartMenu);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);


        }
    }
}
