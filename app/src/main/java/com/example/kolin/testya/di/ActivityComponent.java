package com.example.kolin.testya.di;

import android.app.Activity;

import dagger.Component;

/**
 * Created by kolin on 15.04.2017.
 */
@ActivityScope
@Component (dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

}
