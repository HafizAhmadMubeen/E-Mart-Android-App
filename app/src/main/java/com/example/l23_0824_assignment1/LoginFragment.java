package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button btnLogin = view.findViewById(R.id.btnLogin);

        TextInputEditText etLogin_Email = view.findViewById(R.id.etLogin_Email);
        TextInputEditText etLogin_Password = view.findViewById(R.id.etLogin_Password);

        SharedPreferences sp = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        btnLogin.setOnClickListener((v) -> {
            String email           = etLogin_Email.getText().toString().trim();
            String password        = etLogin_Password.getText().toString().trim();

            // empty field checks
            if (email.isEmpty())
            {
                etLogin_Email.setError("Email is required");
                return;
            }

            if (password.isEmpty())
            {
                etLogin_Password.setError("Password is required");
                return;
            }


            // email format check
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etLogin_Email.setError("Enter a valid email");
                return;
            }


            if(email.equals(sp.getString("email", ""))
                    && password.equals(sp.getString("password", "")))
            {
                    editor.putBoolean("isLogin", true);
                    editor.commit();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

            }
            else
            {
                Toast.makeText(getActivity(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

    }
}