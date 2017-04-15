package com.example.kolin.testya.di;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kolin on 15.04.2017.
 */
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity providesActivity(){
        return activity;
    }
}
