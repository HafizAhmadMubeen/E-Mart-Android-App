package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompleteProfileActivity extends AppCompatActivity {

    private String selectedAccountType = ""; // Stores "Buyer" or "Seller"
    private String selectedGender = "";      // Stores "Male" or "Female"

    private EditText etFullName, etPhone, etAddress;
    private TextView tvBuyer, tvSeller, tvMale, tvFemale;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        // 1. Initialize all views
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);

        tvBuyer = findViewById(R.id.tvBuyer);
        tvSeller = findViewById(R.id.tvSeller);
        tvMale = findViewById(R.id.tvMale);
        tvFemale = findViewById(R.id.tvFemale);

        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // 2. Selection Logic for Account Type (Buyer/Seller)
        tvBuyer.setOnClickListener(v -> {
            selectedAccountType = "Buyer";
            highlightSelected(tvBuyer, tvSeller);
        });

        tvSeller.setOnClickListener(v -> {
            selectedAccountType = "Seller";
            highlightSelected(tvSeller, tvBuyer);
        });

        // 3. Selection Logic for Gender (Male/Female)
        tvMale.setOnClickListener(v -> {
            selectedGender = "Male";
            highlightSelected(tvMale, tvFemale);
        });

        tvFemale.setOnClickListener(v -> {
            selectedGender = "Female";
            highlightSelected(tvFemale, tvMale);
        });

        // 4. Save Profile Button
        btnSaveProfile.setOnClickListener(v -> saveToFirebase());
    }

    // Helper method to change colors when a box is clicked
    private void highlightSelected(TextView selected, TextView unselected) {
        // Change selected box to black with white text
        selected.setBackgroundResource(R.drawable.selected_background);
        selected.setTextColor(Color.WHITE);

        // Change unselected box back to your default background
        unselected.setBackgroundResource(R.drawable.search_bar_background);
        unselected.setTextColor(Color.BLACK);
    }

    private void saveToFirebase() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Basic Validation
        if (name.isEmpty() || selectedAccountType.isEmpty() || selectedGender.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and selections", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return;

        // Create the User object (using your required fields)
        User user = new User(name, phone, selectedGender, "N/A", address, selectedAccountType);

        // Save to Realtime Database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.child(uid).setValue(user).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Save to SharedPreferences for Auto-Login
                saveSession(uid, selectedAccountType);

                // Move to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSession(String uid, String role) {
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.putString("uid", uid);
        editor.putString("accountType", role);
        editor.apply();
    }
}