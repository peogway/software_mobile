package com.example.project_software_mobile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MunicipalityActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_municipality);

        String cityName = getIntent().getStringExtra("CITY_NAME");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 fragmentArea = findViewById(R.id.viewArea);

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(this, cityName);
        fragmentArea.setAdapter(tabPagerAdapter);

        new TabLayoutMediator(tabLayout, fragmentArea, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Here you can set the text for the tabs based on the position
                switch (position) {
                    case 0:
                        tab.setText("Basic Info");
                        break;
                    case 1:
                        tab.setText("Comparison");
                        break;
                    case 2:
                        tab.setText("Quiz");
                        break;
                    case 3:
                        tab.setText("Data Visualization");
                        break;
                }
            }
        }).attach();

    }
}
