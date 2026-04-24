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

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextInputEditText etEmailSignUp = view.findViewById(R.id.etEmailSignUp);
        TextInputEditText etPasswordSignUp = view.findViewById(R.id.etPasswordSignUp);
        TextInputEditText etVerify_PasswordSignUp = view.findViewById(R.id.etVerify_PasswordSignUp);
        Button btnSignup = view.findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener((v) -> {
            String email = etEmailSignUp.getText().toString().trim();
            String password = etPasswordSignUp.getText().toString().trim();
            String verifyPassword = etVerify_PasswordSignUp.getText().toString().trim();

            // Validation logic
            if (email.isEmpty()) {
                etEmailSignUp.setError("Email is required");
                return;
            }
            if (password.isEmpty()) {
                etPasswordSignUp.setError("Password is required");
                return;
            }
            if (verifyPassword.isEmpty()) {
                etVerify_PasswordSignUp.setError("Password is required");
                return;
            }
            if (!password.equals(verifyPassword)) {
                etVerify_PasswordSignUp.setError("Passwords do not match");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmailSignUp.setError("Enter a valid email");
                return;
            }
            if (password.length() < 8) {
                etPasswordSignUp.setError("Password must be at least 8 characters long");
                return;
            }
            if (!password.matches(".*[A-Z].*")) {
                etPasswordSignUp.setError("Password must contain at least one uppercase letter");
                return;
            }
            if (!password.matches(".*[a-z].*")) {
                etPasswordSignUp.setError("Password must contain at least one lowercase letter");
                return;
            }
            if (!password.matches(".*[0-9].*")) {
                etPasswordSignUp.setError("Password must contain at least one number");
                return;
            }

            // Firebase signup logic
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Account created, now send verification email
                            sendVerificationEmail();
                        } else {
                            // Show error
                            Toast.makeText(requireContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Verification email sent to " + user.getEmail(), Toast.LENGTH_LONG).show();

                            // Sign out the user because they aren't verified yet
                            mAuth.signOut();

                            // Redirect user back to Login (Go back to previous fragment)
                            if (getActivity() != null) {
                                getActivity().onBackPressed();
                            }
                        } else {
                            Toast.makeText(requireContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}