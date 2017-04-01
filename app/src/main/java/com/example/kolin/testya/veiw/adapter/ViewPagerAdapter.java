package com.example.kolin.testya.veiw.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kolin on 21.03.2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private List<String> titlesList;
    private boolean isAddTitle;

    public ViewPagerAdapter(FragmentManager fm, boolean isAddTitle) {
        super(fm);
        list = new ArrayList<>();
        this.isAddTitle = isAddTitle;
        if (isAddTitle)
            titlesList = new ArrayList<>();
    }


    public void addFragment(Fragment fragment, String title) {
        if (!list.contains(fragment)) {
            list.add(fragment);
            notifyDataSetChanged();

            if (isAddTitle && title != null) {
                titlesList.add(title);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        return list != null && !list.isEmpty() ? list.get(position) : null;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return isAddTitle ? titlesList.get(position) : super.getPageTitle(position);
    }
}
