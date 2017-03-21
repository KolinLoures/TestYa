package com.example.kolin.testya.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.kolin.testya.HistoryFragment;
import com.example.kolin.testya.InfoFragment;
import com.example.kolin.testya.TranslatorFragment;

/**
 * Created by kolin on 21.03.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TranslatorFragment.newInstance("","");
            case 1:
                return HistoryFragment.newInstance("","");
            case 2:
                return InfoFragment.newInstance("","");
            default:
                return null;
        }
    }




    @Override
    public int getCount() {
        return PAGE_COUNT;
    }




}
