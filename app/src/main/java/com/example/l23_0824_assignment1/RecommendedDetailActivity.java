package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
       String product_name = i.getStringExtra(KeyUtils.ProductName);
       setupProductDetails(product_name);

       btnbuynow.setOnClickListener(v -> {
           Intent buyintent = new Intent(RecommendedDetailActivity.this, BuyNowActivity.class);
           buyintent.putExtra(KeyUtils.ProductName, product_name);
           startActivity(buyintent);
       });
    }

    private void init() {
        ivProduct = findViewById(R.id.ivProductLarge);
        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDesc = findViewById(R.id.tvDetailFullDesc);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        btnbuynow = findViewById(R.id.btnBuyNow);


    }

    private void setupProductDetails(String product_name) {
        if (product_name == null) return;


        if (product_name.equals("DealOfDay")) {
            updateUI(R.drawable.mic_stand, R.string.Mic_name, R.string.Mic_price, R.string.Mic_desc);
        }
        else if (product_name.equals("blackheadphone")) {
            updateUI(R.drawable.blackheadphone, R.string.Headphone_name, R.string.Headphone_price, R.string.Headphone_detailed_desc);
        }
        else if (product_name.equals("whiteheadphone")) {
            updateUI(R.drawable.whiteheadphone, R.string.Headphone_name, R.string.Headphone_price, R.string.Headphone_detailed_desc);
        }
    }


    private void updateUI(int imgRes, int nameRes, int priceRes, int descRes) {
        ivProduct.setImageResource(imgRes);
        tvName.setText(nameRes);
        tvPrice.setText(priceRes);
        tvDesc.setText(descRes);
    }


}