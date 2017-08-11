package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.kolin.testya.data.TypeOfTranslation;
import com.example.kolin.testya.veiw.common.CommonFragment;

/**
 * Created by kolin on 16.07.2017.
 */

public class HistoryFavoriteViewPagerAdapter extends FragmentStatePagerAdapter {

    private CommonFragment commonFragmentHistory;
    private CommonFragment commonFragmentFavorite;

    public HistoryFavoriteViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return CommonFragment.newInstance(TypeOfTranslation.HISTORY);
            case 1:
                return CommonFragment.newInstance(TypeOfTranslation.FAVORITE);
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                commonFragmentHistory = (CommonFragment) fragment;
                break;
            case 1:
                commonFragmentFavorite = (CommonFragment) fragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
