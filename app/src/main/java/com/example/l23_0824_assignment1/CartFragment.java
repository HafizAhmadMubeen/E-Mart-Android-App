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
import java.util.Date;
import java.util.Locale;

public class CartFragment extends Fragment {
    private static final int SMS_PERMISSION_CODE = 101;

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice, tvShippingPrice;
    private Button btnCheckout;
    private CartAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvShippingPrice = view.findViewById(R.id.tvShippingPrice);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CartAdapter(getContext(), CartManager.cartList, new OnCartChangedListener() {
            @Override
            public void onPriceChanged(double newTotal) {
                updatePriceUI(newTotal);
            }
        });

        rvCartItems.setAdapter(adapter);
        calculateInitialTotal();

        btnCheckout.setOnClickListener(v -> {
            if (CartManager.cartList.isEmpty()) {
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

    private void calculateInitialTotal() {
        double total = 0;
        for (CartItem item : CartManager.cartList) {
            String priceStr = item.getProduct().getPrice().replace("$", "").trim();
            double price = Double.parseDouble(priceStr);
            total += (price * item.getQuantity());
        }
        updatePriceUI(total);
    }

    private void updatePriceUI(double total) {
        tvTotalPrice.setText(String.format("$%.2f", total));
    }

    private void showConfirmOrderDialog() {
        StringBuilder productSummary = new StringBuilder();
        double total = 0;

        for (CartItem item : CartManager.cartList) {
            productSummary.append(item.getProduct().getName())
                    .append(" (x").append(item.getQuantity()).append(")\n");

            double price = Double.parseDouble(item.getProduct().getPrice().replace("$", ""));
            total += (price * item.getQuantity());
        }

        final String finalTotal = String.format("%.2f", total);
        final String finalSummary = productSummary.toString();

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Purchase")
                .setMessage("Place this order and save to history?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // 1. Save to Firebase Order History
                    saveOrderToFirebase(finalSummary, finalTotal);

                    // 2. Send SMS
                    sendCartSMS("E-Mart Order:\n" + finalSummary + "Total: $" + finalTotal);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveOrderToFirebase(String summary, String total) {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");

        // Generate Unique Order ID
        String orderId = ordersRef.push().getKey();

        // Get Current Date
        String currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());

        if (uid != null && orderId != null) {
            OrderModel newOrder = new OrderModel(
                    orderId,
                    currentDate,
                    "PROCESSING",
                    total,
                    summary,
                    uid
            );

            ordersRef.child(orderId).setValue(newOrder)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Order recorded in history", Toast.LENGTH_SHORT).show();

                        // Clear Cart after successful save
                        CartManager.cartList.clear();
                        adapter.notifyDataSetChanged();
                        updatePriceUI(0);
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to save order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void sendCartSMS(String message) {
        String phoneNumber = "03001234567";
        try {
            SmsManager smsManager = getContext().getSystemService(SmsManager.class);
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showConfirmOrderDialog();
            } else {
                Toast.makeText(getContext(), "Permission Denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}