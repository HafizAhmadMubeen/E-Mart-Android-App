package com.example.l23_0824_assignment1;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Log_SignupActivity extends AppCompatActivity {

    LoginSignupAdapter loginSignupAdapter;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    TabLayoutMediator mediator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();
        viewPager2.setCurrentItem(1, true);
        viewPager2.setCurrentItem(0, true);

    }

    private void init()
    {
        tabLayout = findViewById(R.id.tabLayout);
        loginSignupAdapter = new LoginSignupAdapter(this);
        viewPager2 = findViewById(R.id.viewPagerAuth);
        viewPager2.setAdapter(loginSignupAdapter);
        mediator = new TabLayoutMediator(
                tabLayout,
                viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0:
                                tab.setText("Log In");
                                break;
                            case 1:
                                tab.setText("Sign Up");
                                break;
                        }
                    }
                }
        );
        mediator.attach();
    }
}