package com.example.kolin.testya;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kolin.testya.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeViewPager) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setPagingEnable(false);

        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }


    @SuppressWarnings("all")
    private void setupTabIcons() {
            tabLayout.getTabAt(0).setIcon(R.drawable.ic_settings_black_24dp);
            tabLayout.getTabAt(1).setIcon(R.drawable.ic_favorite_border_black_24dp);
            tabLayout.getTabAt(2).setIcon(R.drawable.ic_check_circle_black_24dp);

            tabLayout.getTabAt(1).getIcon().setAlpha(128);
            tabLayout.getTabAt(2).getIcon().setAlpha(128);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tab.getIcon().setAlpha(255);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.getIcon().setAlpha(128);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
    }
}
