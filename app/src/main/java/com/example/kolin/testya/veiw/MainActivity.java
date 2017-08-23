package com.example.kolin.testya.veiw;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kolin.testya.R;
import com.example.kolin.testya.veiw.adapter.MainViewPagerAdapter;
import com.example.kolin.testya.veiw.custom_views.NonSwipeViewPager;
import com.example.kolin.testya.veiw.historyfavorite.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.translator.ITranslatorView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements
        HistoryFavoriteFragment.OnHistoryFavoritesSelectedListener,
        HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    //Views
    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;
    private MainViewPagerAdapter adapter;

    private ViewPager.OnPageChangeListener viewPageChangeListener;
    private TabLayout.OnTabSelectedListener tabSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidInjection.inject(this);

        adapter = new MainViewPagerAdapter(getSupportFragmentManager());

        viewPager = (NonSwipeViewPager) findViewById(R.id.main_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);

        viewPager.setAdapter(adapter);
        viewPager.setPagingEnable(false);

        setupTabListener();
        setupViewPageChangeListener();
    }

    private void setupViewPageChangeListener() {
        viewPageChangeListener = new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if (tabAt != null)
                    tabAt.select();

                Fragment fragmentAtPosition = adapter.getFragmentAtPosition(position);

                if (fragmentAtPosition instanceof Updatable)
                    ((Updatable) fragmentAtPosition).update();


            }

            public void onPageScrollStateChanged(int state) {
            }
        };

        viewPager.addOnPageChangeListener(viewPageChangeListener);
    }

    private void setupTabListener() {
        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        };

        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }


    @Override
    protected void onDestroy() {
        viewPager.removeOnPageChangeListener(viewPageChangeListener);
        tabLayout.removeOnTabSelectedListener(tabSelectedListener);

        super.onDestroy();
    }

    @Override
    public void onHistoryFavoriteEntitySelected(int id) {
        ((ITranslatorView) adapter.getFragmentAtPosition(0)).updateTranslation(id);
        viewPager.setCurrentItem(0);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
