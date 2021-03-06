package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.kolin.testya.veiw.MainActivity;
import com.example.kolin.testya.veiw.Updatable;
import com.example.kolin.testya.veiw.historyfavorite.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.fragment.InfoFragment;
import com.example.kolin.testya.veiw.translator.TranslatorFragment;

/**
 * Created by kolin on 21.03.2017.
 * <p>
 * Adapter for view pager in {@link MainActivity}
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {


    private TranslatorFragment translatorFragment;
    private HistoryFavoriteFragment historyFavoriteFragment;
    private InfoFragment infoFragment;

    private SparseArray<Fragment> fragments = new SparseArray<>();


    public MainViewPagerAdapter(FragmentManager fm) {
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
        fragments.put(position, fragment);
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

    public Fragment getFragmentAtPosition(int position){
        return position >= 0 && position <= fragments.size() - 1
                ? fragments.get(position)
                : null;
    }

    @Override
    public int getItemPosition(Object object) {
        int itemPosition = super.getItemPosition(object);
        Fragment fragment = (Fragment) object;
        if (fragment != null && fragment instanceof Updatable)
            switch (itemPosition) {
                case 0:
                    ((Updatable) fragment).update();
                    break;
                case 1:
                    ((Updatable) fragment).update();
                    break;
                case 2:
                    ((Updatable) fragment).update();
                    break;
            }
        return itemPosition;
    }


    @Override
    public int getCount() {
        return 3;
    }
}
