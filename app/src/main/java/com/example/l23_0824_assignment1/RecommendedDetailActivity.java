package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.Key;
import java.util.Objects;

public class RecommendedDetailActivity extends AppCompatActivity {

    private ImageView ivProduct, btnBack;

    private Button btnbuynow;
    private TextView tvName, tvPrice, tvDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recommended_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
       Intent i =getIntent();
       setupProductDetails();

        btnbuynow.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("TransferSP", MODE_PRIVATE);
            String name = sp.getString("p_name", "");
            String price = sp.getString("p_price", "");
            String shortDesc = ""; // You can pull this if needed
            String longDesc = sp.getString("p_desc", "");
            int imgRes = sp.getInt("p_img", 0);


            Product cartProduct = new Product("ID_CART", name, price, "", shortDesc, longDesc, imgRes, false);


            CartManager.addProduct(cartProduct);


            AlertDialog.Builder builder = new AlertDialog.Builder(RecommendedDetailActivity.this);
            builder.setTitle("Added to Cart");
            builder.setMessage("Do you want to add this product to cart?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Toast.makeText(RecommendedDetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.create().show();

        });

       btnBack.setOnClickListener(v -> {
           SharedPreferences sp = getSharedPreferences("TransferSP", MODE_PRIVATE);
           SharedPreferences.Editor editor = sp.edit();
           editor.clear();
           editor.apply();

           finish();
       });
    }

    private void init() {
        ivProduct = findViewById(R.id.ivProductLarge);
        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDesc = findViewById(R.id.tvDetailFullDesc);
        btnBack = findViewById(R.id.btnBack);
        btnbuynow = findViewById(R.id.btnBuyNow);


    }

    private void setupProductDetails() {
        SharedPreferences sp = getSharedPreferences("TransferSP", MODE_PRIVATE);

        String name = sp.getString("p_name", "Product");
        String price = sp.getString("p_price", "$0.00");
        String desc = sp.getString("p_desc", "No description available");
        int imgRes = sp.getInt("p_img", R.drawable.ic_launcher_background);

        tvName.setText(name);
        tvPrice.setText(price);
        tvDesc.setText(desc);
        ivProduct.setImageResource(imgRes);
    }


}