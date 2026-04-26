package com.example.l23_0824_assignment1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CartFragment extends Fragment {
    private static final int SMS_PERMISSION_CODE = 101;
    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private CartAdapter adapter;
    private ArrayList<CartItem> cartList;

    public CartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));

        btnCheckout.setOnClickListener(v -> {
            if (cartList == null || cartList.isEmpty()) {
                Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                showConfirmOrderDialog();
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartFromSQLite();
    }

    private void loadCartFromSQLite() {
        CartDBManager db = new CartDBManager(getContext());
        db.open();
        cartList = db.getAllCartItems();
        db.close();

        if (adapter == null) {
            adapter = new CartAdapter(getContext(), cartList, newTotal ->
                    tvTotalPrice.setText(String.format("$%.2f", newTotal)));
            rvCartItems.setAdapter(adapter);
        } else {
            adapter.updateList(cartList);
        }

        calculateTotal();
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            try {
                String priceStr = item.getProduct().getPrice().replace("$", "").trim();
                if (!priceStr.isEmpty()) {
                    total += (Double.parseDouble(priceStr) * item.getQuantity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tvTotalPrice.setText(String.format(Locale.getDefault(), "$%.2f", total));
    }

    private void showConfirmOrderDialog() {
        StringBuilder sb = new StringBuilder("E-Mart Order Summary:\n");
        double total = 0;

        for (CartItem item : cartList) {
            sb.append("- ").append(item.getProduct().getName())
                    .append(" (").append(item.getProduct().getPrice()).append(")")
                    .append(" x").append(item.getQuantity()).append("\n");
            try {
                double price = Double.parseDouble(
                        item.getProduct().getPrice().replace("$", "").trim());
                total += (price * item.getQuantity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final String finalTotal = String.format("%.2f", total);
        sb.append("Total Price: $").append(finalTotal);
        final String smsMessage = sb.toString();

        new AlertDialog.Builder(getContext())
                .setTitle("Checkout")
                .setMessage("Send order via SMS and save to history?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    saveOrderToFirebase(smsMessage, finalTotal);
                    sendCartSMS(smsMessage);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveOrderToFirebase(String summary, String total) {
        String buyerId = FirebaseAuth.getInstance().getUid();


        CartDBManager db = new CartDBManager(getContext());
        db.open();
        String sellerId = db.getSellerIdFromCart();
        db.close();

        android.util.Log.d("DEBUG_SELLER", "sellerId from SQLite = " + sellerId);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = ordersRef.push().getKey();
        String date = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

        OrderModel order = new OrderModel(orderId, date, "PROCESSING", total, summary, buyerId, sellerId);

        ordersRef.child(orderId).setValue(order).addOnSuccessListener(aVoid -> {
            CartDBManager cartDb = new CartDBManager(getContext());
            cartDb.open();
            cartDb.clearCart();
            cartDb.close();

            loadCartFromSQLite();
            Toast.makeText(getContext(), "Order Placed!", Toast.LENGTH_SHORT).show();
        });
    }

    private void sendCartSMS(String message) {
        try {
            SmsManager sms = getContext().getSystemService(SmsManager.class);
            sms.sendTextMessage("03001234567", null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS Failed", Toast.LENGTH_SHORT).show();
        }
    }
}