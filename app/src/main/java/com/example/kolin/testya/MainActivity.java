package com.example.kolin.testya;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.InfoFragment;
import com.example.kolin.testya.veiw.NonSwipeViewPager;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.OnClickCommonFragment;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeViewPager) findViewById(R.id.main_view_pager);

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), false);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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


}
