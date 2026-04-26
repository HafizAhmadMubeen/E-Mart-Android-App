package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvAddress, tvCountry, tvGender, tvPhone;
    private Button btnLogout;
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inflate the layout
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 2. Initialize IDs from your updated XML
        tvName = view.findViewById(R.id.tvProfileName);
        tvAddress = view.findViewById(R.id.tvProfileAddress);
        tvCountry = view.findViewById(R.id.tvProfileCountry);
        tvGender = view.findViewById(R.id.tvProfileGender);
        tvPhone = view.findViewById(R.id.tvProfilePhone);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 3. Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        if (uid != null) {
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            fetchUserData();
        }

        // 4. Logout Logic
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences sp = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
            sp.edit().putBoolean("isLoggedIn", false).apply();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    private void fetchUserData() {
        // Use single value event to fetch data once
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Map database data to our User class
                    User user = snapshot.getValue(User.class);

                    if (user != null) {
                        // 5. Set text to UI
                        tvName.setText(user.fullName);
                        tvAddress.setText(user.address);
                        tvCountry.setText(user.country);
                        tvGender.setText(user.gender);
                        tvPhone.setText(user.phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}