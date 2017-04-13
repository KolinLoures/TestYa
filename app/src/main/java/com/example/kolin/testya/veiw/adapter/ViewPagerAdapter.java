package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kolin.testya.veiw.fragment.Updatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 21.03.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> titlesList;
    private boolean isAddTitle;

    public ViewPagerAdapter(FragmentManager fm, boolean isAddTitle) {
        super(fm);
        fragments = new ArrayList<>();
        this.isAddTitle = isAddTitle;
        if (isAddTitle)
            titlesList = new ArrayList<>();
    }


    public void addFragment(Fragment fragment, String title) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            notifyDataSetChanged();

            if (isAddTitle && title != null) {
                titlesList.add(title);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments != null && !fragments.isEmpty() ? fragments.get(position) : null;
    }

    @Override
    public int getItemPosition(Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != null && fragment instanceof Updatable)
            ((Updatable) fragment).update();
        return super.getItemPosition(object);
    }



    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return isAddTitle ? titlesList.get(position) : super.getPageTitle(position);
    }
}
