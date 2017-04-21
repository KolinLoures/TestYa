package com.example.kolin.testya.veiw;

import com.example.kolin.testya.veiw.adapter.ViewPagerAdapter;

/**
 * Created by kolin on 07.04.2017.
 *
 * Marker interface for update instance
 *
 * Indicate in {@link ViewPagerAdapter} that this fragment must be updated
 */
public interface Updatable {
    void update();
}
