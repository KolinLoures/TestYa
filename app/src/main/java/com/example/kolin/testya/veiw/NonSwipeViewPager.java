package com.example.kolin.testya.veiw;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kolin on 21.03.2017.
 */

public class NonSwipeViewPager extends ViewPager {

    private boolean enable;

    public NonSwipeViewPager(Context context) {
        super(context);
    }

    public NonSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.enable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.enable && super.onTouchEvent(ev);
    }

    public void setPagingEnable(boolean enable){
        this.enable = enable;
    }

    public boolean isPaginEnable(){
        return enable;
    }
}
