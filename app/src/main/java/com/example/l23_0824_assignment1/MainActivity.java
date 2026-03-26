package com.example.l23_0824_assignment1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    ViewPagerAdapter viewPagerAdapter;

    TabLayoutMediator tabLayoutMediator;

    SharedPreferences logsignup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        if(!logsignup.getBoolean("isLogin", false))
        {
            startActivity(new Intent(MainActivity.this, Log_SignupActivity.class));
            finish();
        }

    }


    private void init()
    {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);

        logsignup = getSharedPreferences("login", MODE_PRIVATE);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayoutMediator = new TabLayoutMediator(
                tabLayout,
                viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy(){
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position)
                        {
                            case 0:
                                tab.setText("Home");
                                tab.setIcon(R.drawable.general_icon);
                                break;
                            case 1:
                                tab.setText("Browse");
                                tab.setIcon(R.drawable.general_icon);
                                break;
                            case 2:
                                tab.setText("Favourites");
                                tab.setIcon(R.drawable.general_icon);
                                BadgeDrawable bd = tab.getOrCreateBadge();
                                break;
                            case 3:
                                tab.setText("Cart");
                                tab.setIcon(R.drawable.general_icon);
                                BadgeDrawable bd1 = tab.getOrCreateBadge();
                                bd1.setNumber(6);
                                bd1.setMaxCharacterCount(2);
                                break;
                            case 4:
                                tab.setText("Profile");
                                tab.setIcon(R.drawable.general_icon);
                                break;
                        }
                    }
                }
        );
        tabLayoutMediator.attach();
    }
}