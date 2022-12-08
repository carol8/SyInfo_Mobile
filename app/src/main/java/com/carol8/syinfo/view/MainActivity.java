package com.carol8.syinfo.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.StrictMode;

import com.carol8.syinfo.BuildConfig;
import com.carol8.syinfo.R;
import com.carol8.syinfo.presenter.main.MainActivityViewPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(BuildConfig.DEBUG)
            StrictMode.enableDefaults();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.mainTabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.mainViewPager2);

        MainActivityViewPageAdapter viewPageAdapter = new MainActivityViewPageAdapter(this);
        viewPager2.setAdapter(viewPageAdapter);
        viewPager2.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("System Information");
            } else {
                tab.setText("Benchmarks");
            }
        }).attach();
    }
}