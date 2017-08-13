package com.example.kolin.testya.veiw.historyfavorite;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.di.ProvideComponent;
import com.example.kolin.testya.di.components.ViewComponent;
import com.example.kolin.testya.domain.model.HistoryFavoriteModel;
import com.example.kolin.testya.veiw.CommonFragmentCallback;
import com.example.kolin.testya.veiw.Updatable;
import com.example.kolin.testya.veiw.adapter.HistoryFavoriteViewPagerAdapter;
import com.example.kolin.testya.veiw.common.ICommonView;
import com.example.kolin.testya.veiw.fragment.ClearDialogFragment;

import java.util.List;

import javax.inject.Inject;

public class HistoryFavoriteFragment extends Fragment
        implements  ClearDialogFragment.ClearDialogListener,
        CommonFragmentCallback {

    private ImageButton btnDelete;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private HistoryFavoriteViewPagerAdapter viewPagerAdapter;

    private TabLayout.OnTabSelectedListener tabSelectedListener;
    private ViewPager.OnPageChangeListener viewPagerChangeListener;




    public HistoryFavoriteFragment() {
        setRetainInstance(true);
    }

    public static HistoryFavoriteFragment newInstance() {
        return new HistoryFavoriteFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPagerAdapter = new HistoryFavoriteViewPagerAdapter(getChildFragmentManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history_favorite, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnDelete = (ImageButton) view.findViewById(R.id.fragment_hf_delete);
        tabLayout = (TabLayout) view.findViewById(R.id.fragment_hf_tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.fragment_hf_view_pager);

        viewPager.setAdapter(viewPagerAdapter);

        setupTabSelectedListener();
        setupViewPageChangeListener();
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                if (viewPager.getCurrentItem() == 0)
                    title = getString(R.string.history);
                else
                    title = getString(R.string.favorite);

                ClearDialogFragment clearDialogFragment = ClearDialogFragment.newInstance(title);
                clearDialogFragment.show(getChildFragmentManager(), "ClearDialogFragment");
            }
        });
    }

    private void setupTabSelectedListener() {
        tabSelectedListener = new TabLayout.OnTabSelectedListener() {
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition()); }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        };

        tabLayout.addOnTabSelectedListener(tabSelectedListener);
    }

    private void setupViewPageChangeListener() {
        viewPagerChangeListener = new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        ((Updatable) viewPagerAdapter.getFragmentAtPosition(0)).update();
                        break;
                    case 1:
                        ((Updatable) viewPagerAdapter.getFragmentAtPosition(1)).update();
                        break;
                }

                TabLayout.Tab tabAt = tabLayout.getTabAt(position);
                if (tabAt != null) {
                    tabAt.select();
                }

            }
            public void onPageScrollStateChanged(int state) {}
        };

        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        tabLayout.removeOnTabSelectedListener(tabSelectedListener);
        viewPager.removeOnPageChangeListener(viewPagerChangeListener);

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClickPositiveBtn() {
        if (viewPager.getCurrentItem() == 0)
            ((ICommonView) viewPagerAdapter.getFragmentAtPosition(0)).deleteData();
        else {
            ((ICommonView) viewPagerAdapter.getFragmentAtPosition(1)).deleteData();
            ((ICommonView) viewPagerAdapter.getFragmentAtPosition(0)).removeFavoritesFromFavortes();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showTranslationInFavorite(HistoryFavoriteModel model) {
        ((ICommonView) viewPagerAdapter.getFragmentAtPosition(1)).showLoadedEntity(model);
    }

    @Override
    public void removeTranslationInFavorite(HistoryFavoriteModel model) {
        ((ICommonView) viewPagerAdapter.getFragmentAtPosition(1)).removeEntity(model);
    }

    @Override
    public void updateTranslationInHistory(HistoryFavoriteModel model) {
        ((ICommonView) viewPagerAdapter.getFragmentAtPosition(0)).updateCheckEntity(model);
    }

    @Override
    public void setVisibilityToDeleteButton(boolean visible) {
        if (visible)
            btnDelete.setVisibility(View.VISIBLE);
        else
            btnDelete.setVisibility(View.INVISIBLE);
    }
}
