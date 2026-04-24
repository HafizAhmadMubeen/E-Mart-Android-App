package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        Button btnLogin = view.findViewById(R.id.btnLogin);
        TextInputEditText etLogin_Email = view.findViewById(R.id.etLogin_Email);
        TextInputEditText etLogin_Password = view.findViewById(R.id.etLogin_Password);

        btnLogin.setOnClickListener((v) -> {
            String email = etLogin_Email.getText().toString().trim();
            String password = etLogin_Password.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) return;

            // TRACKER 1
            Toast.makeText(getActivity(), "Attempting Login...", Toast.LENGTH_SHORT).show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // TRACKER 2
                                user.reload().addOnCompleteListener(reloadTask -> {
                                    if (user.isEmailVerified()) {
                                        // TRACKER 3
                                        Toast.makeText(getActivity(), "Verified! Checking DB...", Toast.LENGTH_SHORT).show();
                                        checkUserInDatabase(user.getUid());
                                    } else {
                                        Toast.makeText(getActivity(), "Email NOT verified yet!", Toast.LENGTH_LONG).show();
                                        mAuth.signOut();
                                    }
                                });
                            }
                        } else {
                            // This will tell you if the password is wrong or user doesn't exist
                            Toast.makeText(getActivity(), "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(e -> {
                        // This catches network issues or console config issues
                        Toast.makeText(getActivity(), "System Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void checkUserInDatabase(String uid) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");

        dbRef.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // NOT FIRST TIME: Data exists, save session and go to Dashboard
                    String role = task.getResult().child("accountType").getValue(String.class);
                    saveToPrefs(uid, role);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    // YES FIRST TIME: Verified but no data, go to Profile Add Page
                    // (Change CompleteProfileActivity to your actual Activity name)
                    Intent intent = new Intent(getActivity(), CompleteProfileActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });
    }

    private void saveToPrefs(String uid, String role) {
        SharedPreferences sp = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.putString("accountType", role);
        editor.putBoolean("isLogin", true);
        editor.apply();
    }
}