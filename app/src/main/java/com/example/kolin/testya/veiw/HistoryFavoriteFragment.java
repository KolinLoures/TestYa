package com.example.kolin.testya.veiw;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.kolin.testya.R;
import com.example.kolin.testya.data.TypeSaveTranslation;
import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;
import com.example.kolin.testya.veiw.fragment.Updatable;


public class HistoryFavoriteFragment extends Fragment implements Updatable {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;



    public HistoryFavoriteFragment() {
    }

    public static HistoryFavoriteFragment newInstance() {
        return new HistoryFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_history_favorite, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.history_favorite_view_pager);
        adapter = new ViewPagerAdapter(getFragmentManager(), true);

        adapter.addFragment(
                CommonFragment.newInstance(TypeSaveTranslation.HISTORY),
                getString(R.string.history));

        adapter.addFragment(
                CommonFragment.newInstance(TypeSaveTranslation.FAVORITE),
                getString(R.string.favorite));

        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) view.findViewById(R.id.history_favorite_tab);
        tabLayout.setupWithViewPager(viewPager);

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

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void update() {
        adapter.notifyDataSetChanged();
    }
}
