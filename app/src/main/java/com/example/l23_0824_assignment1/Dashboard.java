package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {


    private LinearLayout dealCard, cardBlack, cardBeige;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        setListeners();
    }
    private void setListeners() {

        dealCard.setOnClickListener(v -> {
            handleProductClick("DealOfDay");
        });

        cardBlack.setOnClickListener(v -> {
            handleProductClick("blackheadphone");
        });

        cardBeige.setOnClickListener(v -> {
            handleProductClick("whiteheadphone");
        });
    }

    private void handleProductClick(String product_name) {
        Intent intent = new Intent(Dashboard.this, RecommendedDetailActivity.class);

        intent.putExtra(KeyUtils.ProductName, product_name);

        startActivity(intent);
    }



    private void init()
    {
        dealCard = findViewById(R.id.DealOfDay);
        cardBlack = findViewById(R.id.blackheadphone);
        cardBeige= findViewById(R.id.whiteheadphone);

    }
}