package com.example.kolin.testya.di;

import com.example.kolin.testya.veiw.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by kolin on 23.08.2017.
 */
@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = FragmentsProvider.class)
    abstract MainActivity bindMainActivity();
}
