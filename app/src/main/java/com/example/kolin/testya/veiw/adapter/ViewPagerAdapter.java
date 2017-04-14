package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.example.kolin.testya.veiw.HistoryFavoriteFragment;
import com.example.kolin.testya.veiw.InfoFragment;
import com.example.kolin.testya.veiw.fragment.TranslatorFragment;
import com.example.kolin.testya.veiw.fragment.Updatable;

/**
 * Created by kolin on 21.03.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

//    private List<Fragment> fragments;
//    private List<String> titlesList;
//    private boolean isAddTitle;

    private TranslatorFragment translatorFragment;
    private HistoryFavoriteFragment historyFavoriteFragment;
    private InfoFragment infoFragment;

    public ViewPagerAdapter(FragmentManager fm, boolean isAddTitle) {
        super(fm);
//        fragments = new ArrayList<>();
//        this.isAddTitle = isAddTitle;
//        if (isAddTitle)
//            titlesList = new ArrayList<>();
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
//        fragments.add(position, fragment);
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

//    public void addFragment(Fragment fragment, String title) {
//        if (!fragments.contains(fragment)) {
//            fragments.add(fragment);
//            notifyDataSetChanged();
//
//            if (isAddTitle && title != null) {
//                titlesList.add(title);
//            }
//        }
//    }

//    @Override
//    public Fragment getItem(int position) {
//        return fragments != null && !fragments.isEmpty() ? fragments.get(position) : null;
//    }

    @Override
    public int getItemPosition(Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != null && fragment instanceof Updatable)
            ((Updatable) fragment).update();
        return super.getItemPosition(object);
    }



    @Override
    public int getCount() {
        return 3;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        return isAddTitle ? titlesList.get(position) : super.getPageTitle(position);
//    }
}
