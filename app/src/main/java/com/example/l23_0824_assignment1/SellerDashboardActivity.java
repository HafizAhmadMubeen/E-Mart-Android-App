package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tvHeaderName, tvHeaderEmail;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_dashboard);

        // 1. Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        // 2. Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);
        toolbar = findViewById(R.id.toolbar);

        // 3. Setup Toolbar
        setSupportActionBar(toolbar);

        // 4. Setup Drawer Toggle (Instructor Style)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 5. Setup Navigation Header Data
        View headerView = navigationView.getHeaderView(0);
        tvHeaderName = headerView.findViewById(R.id.nav_user_name);
        tvHeaderEmail = headerView.findViewById(R.id.nav_user_email);

        if (uid != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            loadHeaderData();
        }

        // 6. Set Listener for Menu Items
        navigationView.setNavigationItemSelectedListener(this);

//        // 7. Load Default Fragment (Home)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SellerHomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // 8. Theme Switching Buttons (Found inside the Navigation View)
        TextView btnLight = findViewById(R.id.btnLight);
        TextView btnDark = findViewById(R.id.btnDark);

        btnLight.setOnClickListener(v -> {
            Toast.makeText(this, "Light Theme Selected", Toast.LENGTH_SHORT).show();
            // Theme logic goes here later
        });

        btnDark.setOnClickListener(v -> {
            Toast.makeText(this, "Dark Theme Selected", Toast.LENGTH_SHORT).show();
            // Theme logic goes here later
        });
    }

    private void loadHeaderData() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        tvHeaderName.setText(user.fullName);
                        tvHeaderEmail.setText(mAuth.getCurrentUser().getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerDashboardActivity.this, "Header Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new SellerHomeFragment()).commit();
        } else if (id == R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new CheckOrderHistory()).commit();
        }
        else if (id == R.id.nav_logout) {
            logoutUser();
        }
        else if ( id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ProfileFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser() {
        mAuth.signOut();
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        sp.edit().clear().apply();

        Intent intent = new Intent(SellerDashboardActivity.this, MainActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}