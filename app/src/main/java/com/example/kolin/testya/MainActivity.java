package com.example.kolin.testya;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.CommonFragment;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.InfoFragment;
import com.example.kolin.testya.veiw.NonSwipeViewPager;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.DataUpdatable;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;


public class MainActivity extends AppCompatActivity implements
        CommonFragment.OnInteractionCommonFragment {

    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeViewPager) findViewById(R.id.main_view_pager);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), false);
        adapter.addFragment(TranslatorFragment.newInstance(), null);
        adapter.addFragment(HistoryFavoriteFragment.newInstance(), null);
        adapter.addFragment(InfoFragment.newInstance("", ""), null);

        viewPager.setAdapter(adapter);
        viewPager.setPagingEnable(false);

        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //TODO: substitute first tab icon or selector
    @SuppressWarnings("all")
    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_translate_black_24dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_bookmark_gray_24dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_settings_black_24dp);
//
//        tabLayout.getTabAt(1).getIcon().setAlpha(128);
//        tabLayout.getTabAt(2).getIcon().setAlpha(128);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getIcon().setAlpha(255);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setAlpha(128);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
    }


    @SuppressWarnings("unchecked")
    @Override
    public void onClickItemInCommonFragment(InternalTranslation internalTranslation, boolean clicked) {
        Pair<Boolean, InternalTranslation> pair = new Pair<>(clicked, internalTranslation);
        if (clicked)
            viewPager.setCurrentItem(0);

        ((DataUpdatable) adapter.getItem(0)).update(pair);
    }
}
