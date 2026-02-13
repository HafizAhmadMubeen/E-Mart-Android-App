package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash extends AppCompatActivity {

    ImageView ivLogo;

    Animation movingtruck;

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
        new Handler().postDelayed(() -> {movetoDashboard();}, 5000);
    }

    private void movetoDashboard()
    {
        startActivity(new Intent(Splash.this, Dashboard.class));
        finish();
    }

    private void applyAnimation() {
        ivLogo.setAnimation(movingtruck);
        ivLogo.startAnimation(movingtruck);

    }
    private void init()
    {
        ivLogo = findViewById(R.id.ivLogo);
        movingtruck = AnimationUtils.loadAnimation(this, R.anim.movingtruck);

    }
}