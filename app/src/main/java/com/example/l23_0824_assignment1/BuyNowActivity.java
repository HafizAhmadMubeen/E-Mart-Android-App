package com.example.l23_0824_assignment1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BuyNowActivity extends AppCompatActivity {

     TextView tvDialogMessage;
     Button btnCancel, btnConfirm;

    private static final int SMS_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_now);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        Intent i = getIntent();
        String product_name = i.getStringExtra(KeyUtils.ProductName);
        setupProductDetails(product_name);

        btnConfirm.setOnClickListener(v -> {
            // Step 1: Check if permission is already granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                sendSMS(product_name);
            } else {
                // Step 2: Request permission from the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            }
        });
    }


    private void setupProductDetails(String product_name) {
        if (product_name == null) return;
        else
            tvDialogMessage.setText("Are you sure you want to buy " + product_name);
    }
    private void init()
    {
        tvDialogMessage = findViewById(R.id.tvDialogMessage);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm=findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(v -> finish());
    }

    private void sendSMS(String product_name) {
        String phoneNumber = "03001234567";
        String message = "Success! You have bought " + product_name;

        try {
            SmsManager smsManager = this.getSystemService(SmsManager.class);
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Purchase Confirmed! SMS Sent.", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e) {

            Toast.makeText(this, "SMS failed to send: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}