package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    ImageView ivLogo;
    TextView tvFast, tvMart;

    Animation movingtruck,fastIn, martIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        applyAnimation();
        new Handler().postDelayed(() -> {movetoDashboard();}, 3000);
    }

    private void movetoDashboard()
    {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        boolean isfirsttimeapp = sp.getBoolean("isfirsttimeapp", true);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (isfirsttimeapp)
        {
            startActivity(new Intent(Splash.this, OnBoarding_Activity.class));
        }
        else if (isLogin)
        {
            SharedPreferences sp1 = getSharedPreferences("login", MODE_PRIVATE);
            String account = sp1.getString("accountType", "buyer");
            Toast.makeText(this, account, Toast.LENGTH_SHORT).show();
            if(account.equals("Seller"))
            {

                startActivity(new Intent(Splash.this, SellerDashboardActivity.class));
            }
            else {
                startActivity(new Intent(Splash.this, MainActivity.class));
            }
        }
        else
        {
             startActivity(new Intent(Splash.this, Log_SignupActivity.class));
        }

        finish();
    }

    private void applyAnimation() {

        ivLogo.setAnimation(movingtruck);
        tvFast.setAnimation(fastIn);
        tvMart.setAnimation(martIn);
        ivLogo.startAnimation(movingtruck);
        tvFast.startAnimation(fastIn);
        tvMart.startAnimation(martIn);


    }
    private void init()
    {
        ivLogo = findViewById(R.id.ivLogo);
        tvFast = findViewById(R.id.tvFast);
        tvMart = findViewById(R.id.tvMart);


        movingtruck = AnimationUtils.loadAnimation(this, R.anim.movingtruck);
        fastIn = AnimationUtils.loadAnimation(this, R.anim.fast_in);
        martIn = AnimationUtils.loadAnimation(this, R.anim.mart_in);

    }
}