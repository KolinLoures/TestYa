package com.example.kolin.testya.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kolin on 15.04.2017.
 */

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext(){
        return this.context;
    }
}
