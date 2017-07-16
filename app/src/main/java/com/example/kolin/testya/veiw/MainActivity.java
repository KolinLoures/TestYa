package com.example.kolin.testya.veiw;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.kolin.testya.R;
import com.example.kolin.testya.TestYaApp;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.DaggerViewComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.InternalTranslation;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.translator.TranslatorFragment;


public class MainActivity extends AppCompatActivity implements
        HistoryFavoriteFragment.OnInteractionHistoryFavoriteFragment,
        ProvideComponent<ViewComponent> {

    //Views
    private TabLayout tabLayout;
    private NonSwipeViewPager viewPager;
    private ViewPagerAdapter adapter;

    private ViewComponent viewComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViewComponent();

        viewPager = (NonSwipeViewPager) findViewById(R.id.main_view_pager);

        FragmentManager supportFragmentManager = getSupportFragmentManager();

        adapter = new ViewPagerAdapter(supportFragmentManager);

        viewPager.setAdapter(adapter);
        viewPager.setPagingEnable(false);

        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        setupTabListener();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 1)
                    adapter.notifyDataSetChanged();

                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if (tabAt != null)
                    tabAt.select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void setupTabListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClickItem(InternalTranslation internalTranslation, boolean clicked) {
        if (clicked)
            viewPager.setCurrentItem(0);

        TranslatorFragment item = (TranslatorFragment) adapter.instantiateItem(viewPager, 0);
    }


    private void initializeViewComponent() {
        this.viewComponent = DaggerViewComponent.builder()
                .appComponent(((TestYaApp) getApplication()).getAppComponent())
                .build();
    }

    @Override
    public ViewComponent getComponent() {
        return viewComponent;
    }
}
