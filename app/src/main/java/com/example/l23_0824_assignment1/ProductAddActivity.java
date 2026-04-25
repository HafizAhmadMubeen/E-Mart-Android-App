package com.example.l23_0824_assignment1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductAddActivity extends AppCompatActivity {

    private EditText etName, etType, etPrice, etDesc;
    private AppCompatButton btnAdd;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);

        etName = findViewById(R.id.etProductName);
        etType = findViewById(R.id.etProductType);
        etPrice = findViewById(R.id.etProductPrice);
        etDesc = findViewById(R.id.etProductDesc);
        btnAdd = findViewById(R.id.btnAddProduct);

        dbRef = FirebaseDatabase.getInstance().getReference("Products");

        btnAdd.setOnClickListener(v -> showConfirmationDialog());
    }

    private void showConfirmationDialog() {
        // 1. Check if fields are empty
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter product name", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Build the Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Action");
        builder.setMessage("Are you sure you want to add this product?");

        builder.setPositiveButton("Yes", (dialog, which) -> saveProductToFirebase());
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void saveProductToFirebase() {
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        String desc = etDesc.getText().toString().trim();
        String sellerId = FirebaseAuth.getInstance().getUid();

        // Unique key for each product
        String productId = dbRef.push().getKey();

        if (productId != null) {

            SellerProductModelclass product = new SellerProductModelclass(productId, name, type, price, desc, sellerId);
            dbRef.child(productId).setValue(product).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    finish(); // This takes the user back to the SellerHomeFragment
                } else {
                    Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}