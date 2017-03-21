package com.example.kolin.testya.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;

import com.example.kolin.testya.HistoryFragment;
import com.example.kolin.testya.InfoFragment;
import com.example.kolin.testya.R;
import com.example.kolin.testya.TranslatorFragment;

/**
 * Created by kolin on 21.03.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.context = context;
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

    @Override
    public CharSequence getPageTitle(int position) {

        return super.getPageTitle(position);
    }
}
