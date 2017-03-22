package com.example.kolin.testya;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kolin.testya.adapter.ViewPagerAdapter;


public class HistoryFavoriteFragment extends Fragment {



    public HistoryFavoriteFragment() {
    }

    public static HistoryFavoriteFragment newInstance(String param1, String param2) {
        return new HistoryFavoriteFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.history_favorite_view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager(), true);
        viewPagerAdapter.addFragment(CommonFragment.newInstance("История", ""), " История ");
        viewPagerAdapter.addFragment(CommonFragment.newInstance("Избранное", ""), "Избранное");
        viewPager.setAdapter(viewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.history_favorite_tab);
        tabLayout.setupWithViewPager(viewPager);

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

}
