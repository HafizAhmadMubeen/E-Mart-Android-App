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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private static final int SMS_PERMISSION_CODE = 101;

    private RecyclerView rvCartItems;
    private TextView tvTotalPrice, tvShippingPrice;
    private Button btnCheckout;
    private CartAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

    @Override
    public void onResume() {
        super.onResume();

        if (CartManager.cartList != null && adapter != null) {
            adapter.notifyDataSetChanged();
            calculateInitialTotal();
        }
    }

    private void showConfirmOrderDialog() {

        StringBuilder sb = new StringBuilder("E-Mart Order:\n");
        double total = 0;

        for (CartItem item : CartManager.cartList) {
            sb.append("- ").append(item.getProduct().getName())
                    .append(" (x").append(item.getQuantity()).append(")\n");

            double price = Double.parseDouble(item.getProduct().getPrice().replace("$", ""));
            total += (price * item.getQuantity());
        }
        sb.append("Total: $").append(String.format("%.2f", total));

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Purchase")
                .setMessage("Send order confirmation SMS?")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    sendCartSMS(sb.toString());
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void sendCartSMS(String message) {
        String phoneNumber = "03001234567";

        try {
            SmsManager smsManager = getContext().getSystemService(SmsManager.class);
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            Toast.makeText(getContext(), "Purchase Confirmed! SMS Sent.", Toast.LENGTH_LONG).show();

            CartManager.cartList.clear();
            adapter.notifyDataSetChanged();
            updatePriceUI(0);

        } catch (Exception e) {
            Toast.makeText(getContext(), "SMS failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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