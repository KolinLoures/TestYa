package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.kolin.testya.veiw.MainActivity;
import com.example.kolin.testya.veiw.fragment.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.fragment.InfoFragment;
import com.example.kolin.testya.veiw.translator.TranslatorFragment;

/**
 * Created by kolin on 21.03.2017.
 *
 * Adapter for view pager in {@link MainActivity}
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    private TranslatorFragment translatorFragment;
    private HistoryFavoriteFragment historyFavoriteFragment;
    private InfoFragment infoFragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TranslatorFragment();
            case 1:
                return new HistoryFavoriteFragment();
            case 2:
                return new InfoFragment();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                translatorFragment = (TranslatorFragment) fragment;
                break;
            case 1:
                historyFavoriteFragment = (HistoryFavoriteFragment) fragment;
                break;
            case 2:
                infoFragment = (InfoFragment) fragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }



    @Override
    public int getCount() {
        return 3;
    }
}
