package com.example.kolin.testya.di.components;

import android.app.Activity;

import com.example.kolin.testya.di.ActivityScope;
import com.example.kolin.testya.di.modules.ActivityModule;

import dagger.Component;

/**
 * Created by kolin on 15.04.2017.
 */
@ActivityScope
@Component (dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

}
